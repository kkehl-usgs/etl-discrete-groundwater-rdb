package gov.usgs.wma.waterdata.groundwater;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DiscreteGroundWaterRowMapperTest {

	ByteArrayOutputStream out;
	Writer destination;
	ResultSet mockRs;
	DiscreteGroundWater dgw;

	@BeforeEach
	public void setup() {
		out = new ByteArrayOutputStream();
		destination = new OutputStreamWriter(out);

		mockRs = Mockito.mock(ResultSet.class);
		dgw = makeDgw();
		try {
			Mockito.when(mockRs.getString("agency_cd")).thenReturn(dgw.agencyCode);
			Mockito.when(mockRs.getString("agency_code")).thenReturn(dgw.agencyCode);
			Mockito.when(mockRs.getString("approval_status_code")).thenReturn(dgw.approvalStatusCode);
			Mockito.when(mockRs.getString("date_measured")).thenReturn(dgw.dateMeasured);
			Mockito.when(mockRs.getTimestamp("date_measured_raw")).thenReturn(dgw.dateMeasuredRaw);
			Mockito.when(mockRs.getString("date_time_accuracy_code")).thenReturn(dgw.dateTimeAccuracyCode);
			Mockito.when(mockRs.getString("level_accuracy_code")).thenReturn(dgw.levelAccuracyCode);
			Mockito.when(mockRs.getString("level_feet_above_vertical_datum")).thenReturn(dgw.levelFeetAboveVerticalDatum);
			Mockito.when(mockRs.getString("level_feet_below_land_surface")).thenReturn(dgw.levelFeetBelowLandSurface);
			Mockito.when(mockRs.getString("measurement_method_code")).thenReturn(dgw.measurementMethodCode);
			Mockito.when(mockRs.getString("measurement_source_code")).thenReturn(dgw.measurementSourceCode);
			Mockito.when(mockRs.getString("measuring_agency_code")).thenReturn(dgw.measuringAgencyCode);
			Mockito.when(mockRs.getString("site_identification_number")).thenReturn(dgw.siteIdentificationNumber);
			Mockito.when(mockRs.getString("site_status_code")).thenReturn(dgw.siteStatusCode);
			Mockito.when(mockRs.getString("time_measured_utc")).thenReturn(dgw.timeMeasuredUtc);
			Mockito.when(mockRs.getString("timezone_code")).thenReturn(dgw.timezoneCode);
			Mockito.when(mockRs.getString("vertical_datum_code")).thenReturn(dgw.verticalDatumCode);
			Mockito.when(mockRs.getString("parameter_code")).thenReturn(dgw.parameterCode);

		} catch (SQLException e) {
			throw new RuntimeException("Error mocking resultset", e);
		}
	}

	DiscreteGroundWater makeDgw() {
		DiscreteGroundWater dgw = new DiscreteGroundWater();
		dgw.agencyCode = "USGS";
		dgw.siteIdentificationNumber = "4042342342";
		// date
		// time
		dgw.levelFeetBelowLandSurface = "23.06";
		// entry code [S]see or [L]land
		dgw.verticalDatumCode = ""; // only if vertical measurement
		dgw.levelFeetAboveVerticalDatum = "";  // usually only one set in the file
		dgw.measurementSourceCode = "";
		dgw.measuringAgencyCode = "USGS";
		dgw.levelAccuracyCode = "2"; // two digits after decimal point
		dgw.siteStatusCode = ""; // R, S or blank
		dgw.measurementMethodCode = "S"; // S, R, or V
		dgw.dateMeasured = "07-MAY-2007 18:30:47";
		LocalDateTime dateTime = LocalDateTime.of(2007, Month.MAY, 01, 12, 0);
		dgw.dateMeasuredRaw = Timestamp.valueOf(dateTime);
		dgw.dateTimeAccuracyCode = "D"; // [D]day or [M]minute
		dgw.timezoneCode = "UTC";
		dgw.timeMeasuredUtc = "01-MAY-2007 12:00:00"; // UTC
		dgw.approvalStatusCode = "T"; // T or R

		dgw.parameterCode = "12345";

		return dgw;
	}

	@Test
	void testResultSetMapping() throws SQLException {
		// SETUP
		DiscreteGroundWaterRowMapper rowMapper = new DiscreteGroundWaterRowMapper();

		// ACTION UNDER TEST
		DiscreteGroundWater actual = rowMapper.mapRow(mockRs, 0);

		// ASSERTIONS
		assertEquals(dgw.agencyCode, actual.agencyCode);
		assertEquals(dgw.siteIdentificationNumber, actual.siteIdentificationNumber);
		assertEquals(dgw.levelFeetBelowLandSurface, actual.levelFeetBelowLandSurface);
		assertEquals(dgw.verticalDatumCode, actual.verticalDatumCode); // only if vertical measurement
		assertEquals(dgw.levelFeetAboveVerticalDatum, actual.levelFeetAboveVerticalDatum);  // usually only one set in the file
		assertEquals(dgw.measurementSourceCode, actual.measurementSourceCode);
		assertEquals(dgw.measuringAgencyCode, actual.measuringAgencyCode);
		assertEquals(dgw.levelAccuracyCode, actual.levelAccuracyCode); // two digits after decimal point
		assertEquals(dgw.siteStatusCode, actual.siteStatusCode); // R, S or blank
		assertEquals(dgw.measurementMethodCode, actual.measurementMethodCode); // S, R, or V
		assertEquals(dgw.dateMeasured, actual.dateMeasured);
		assertEquals(dgw.dateMeasuredRaw, actual.dateMeasuredRaw);
		assertEquals(dgw.dateTimeAccuracyCode, actual.dateTimeAccuracyCode); // [D]day or [M]minute
		assertEquals(dgw.timezoneCode, actual.timezoneCode);
		assertEquals(dgw.timeMeasuredUtc, actual.timeMeasuredUtc); // UTC
		assertEquals(dgw.approvalStatusCode, actual.approvalStatusCode); // T or R
		assertEquals(dgw.parameterCode, actual.parameterCode);
	}
}
