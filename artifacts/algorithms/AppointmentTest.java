package appointment;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AppointmentTest {

    private static Date datePlusDays(int daysFromNow) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysFromNow);
        return cal.getTime();
    }

    @ParameterizedTest(name = "[{index}] valid appointment id={0}")
    @MethodSource("validAppointmentArgs")
    @DisplayName("Creates valid appointments")
    void testValidAppointmentCreation(String id, Date date, String desc) {
        Appointment appointment = new Appointment(id, date, desc);
        assertEquals(id, appointment.getAppointmentId());
        assertEquals(date, appointment.getAppointmentDate());
        assertEquals(desc, appointment.getDescription());
    }

    private static Stream<Arguments> validAppointmentArgs() {
        return Stream.of(
            Arguments.of("123", datePlusDays(1), "Dentist Visit"),
            Arguments.of("A1", datePlusDays(30), "Physical"),
            Arguments.of("0000000000", datePlusDays(1), "Checkup") // 10 chars is allowed
        );
    }

    @ParameterizedTest(name = "[{index}] invalid appointment id={0}, date={1}, descLen={2}")
    @MethodSource("invalidAppointmentArgs")
    @DisplayName("Rejects invalid appointments (data-driven)")
    void testInvalidAppointmentCreation(String id, Date date, String desc) {
        assertThrows(IllegalArgumentException.class, () -> new Appointment(id, date, desc));
    }

    private static Stream<Arguments> invalidAppointmentArgs() {
        String tooLongId = "12345678901"; // 11 chars
        String tooLongDesc = "This description is definitely longer than fifty characters!";

        return Stream.of(
            // ID validation
            Arguments.of(null, datePlusDays(1), "Valid desc"),
            Arguments.of(tooLongId, datePlusDays(1), "Valid desc"),

            // Date validation
            Arguments.of("123", null, "Valid desc"),
            Arguments.of("123", datePlusDays(-1), "Past Event"),

            // Description validation
            Arguments.of("123", datePlusDays(1), null),
            Arguments.of("123", datePlusDays(1), tooLongDesc)
        );
    }
}
