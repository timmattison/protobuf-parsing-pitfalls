package com.timmattison.pitfalls.domain;

import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by timmattison on 2/11/15.
 */
public class MeasurementsTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * This shows that our medical measurement baseline actually does generate binary data
     */
    @Test
    public void medicalBaselineShouldNotBeNull() {
        MeasurementsProtos.Medical medical = getBaselineMedicalMeasurement();

        byte[] binaryMedical = medical.toByteArray();

        Assert.assertNotNull(binaryMedical);
    }

    /**
     * This shows that our astronomical measurement baseline actually does generate binary data
     */
    @Test
    public void astronomicalBaselineShouldNotBeNull() {
        MeasurementsProtos.Astronomical astronomical = getBaselineAstronomicalMeasurement();

        byte[] binaryAstronomical = astronomical.toByteArray();

        Assert.assertNotNull(binaryAstronomical);
    }

    /**
     * This shows that type safety before binary conversion works
     */
    @Test
    public void shouldNotBeAbleToAssignBetweenMeasurementTypes() {
        thrown.expect(ClassCastException.class);
        MeasurementsProtos.Medical medical = getBaselineMedicalMeasurement();
        MeasurementsProtos.Astronomical astronomical = getBaselineAstronomicalMeasurement();

        Object temp = medical;
        astronomical = (MeasurementsProtos.Astronomical) temp;
    }

    /**
     * This shows that the data that comes out is the same size
     */
    @Test
    public void shouldBeTheSameSize() {
        MeasurementsProtos.Medical medical = getBaselineMedicalMeasurement();
        MeasurementsProtos.Astronomical astronomical = getBaselineAstronomicalMeasurement();

        byte[] binaryMedical = medical.toByteArray();
        byte[] binaryAstronomical = astronomical.toByteArray();

        Assert.assertEquals(binaryMedical.length, binaryAstronomical.length);
    }

    @Test
    public void shouldNotAcceptBinaryMedicalDataAsAstronomicalData() throws InvalidProtocolBufferException {
        thrown.expect(InvalidProtocolBufferException.class);
        MeasurementsProtos.Medical medical = getBaselineMedicalMeasurement();

        byte[] binaryMedical = medical.toByteArray();

        MeasurementsProtos.Astronomical astronomical = MeasurementsProtos.Astronomical.parseFrom(binaryMedical);

        System.out.println("If we got here we have a problem");
        System.out.println(JsonFormat.printToString(astronomical));
    }


    @Test
    public void shouldNotAcceptBinaryAstronomicalDataAsMedicalData() throws InvalidProtocolBufferException {
        thrown.expect(InvalidProtocolBufferException.class);
        MeasurementsProtos.Astronomical astronomical = getBaselineAstronomicalMeasurement();

        byte[] binaryAstronomical = astronomical.toByteArray();

        MeasurementsProtos.Medical medical = MeasurementsProtos.Medical.parseFrom(binaryAstronomical);

        System.out.println("If we got here we have a problem");
        System.out.println(JsonFormat.printToString(medical));
    }

    private MeasurementsProtos.Medical getBaselineMedicalMeasurement() {
        MeasurementsProtos.Medical.Builder builder = MeasurementsProtos.Medical.newBuilder();
        builder.setHeightInMillimeters(1778);
        builder.setWeightInGrams(74843);

        return builder.build();
    }

    private MeasurementsProtos.Astronomical getBaselineAstronomicalMeasurement() {
        MeasurementsProtos.Astronomical.Builder builder = MeasurementsProtos.Astronomical.newBuilder();
        builder.setId(1);
        builder.setSizeInAttoParsecs(50000000L);

        return builder.build();
    }
}
