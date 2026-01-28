package appointment;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AppointmentServiceTest {

    private static Date datePlusDays(int daysFromNow) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysFromNow);
        return cal.getTime();
    }

    @Test
    @DisplayName("Adds a list of appointments using an iterative execution algorithm")
    void testAddMultipleAppointmentsIterative() {
        AppointmentService service = new AppointmentService();

        // data set (collection) of test inputs
        List<Appointment> dataSet = List.of(
            new Appointment("001", datePlusDays(1), "Checkup"),
            new Appointment("002", datePlusDays(2), "Follow-up"),
            new Appointment("003", datePlusDays(3), "Lab work")
        );

        // algorithm: iterate through the dataset, execute, verify
        for (Appointment appt : dataSet) {
            service.addAppointment(appt);
        }

        assertEquals(dataSet.size(), service.getAllAppointments().size());
    }

    @ParameterizedTest(name = "[{index}] rejects duplicate id {0}")
    @MethodSource("duplicateIdArgs")
    @DisplayName("Rejects duplicate IDs (data-driven)")
    void testDuplicateIdRejected(String id) {
        AppointmentService service = new AppointmentService();

        service.addAppointment(new Appointment(id, datePlusDays(1), "Initial"));
        assertThrows(IllegalArgumentException.class, () ->
            service.addAppointment(new Appointment(id, datePlusDays(2), "Duplicate"))
        );
    }

    private static Stream<Arguments> duplicateIdArgs() {
        return Stream.of(
            Arguments.of("001"),
            Arguments.of("A100"),
            Arguments.of("0000000000") // 10 chars valid
        );
    }

    @ParameterizedTest(name = "[{index}] delete id {0} leaves size {1}")
    @MethodSource("deleteArgs")
    @DisplayName("Deletes appointments by id (data-driven)")
    void testDeleteAppointment(String idToDelete, int expectedSizeAfterDelete) {
        AppointmentService service = new AppointmentService();

        service.addAppointment(new Appointment("100", datePlusDays(1), "One"));
        service.addAppointment(new Appointment("200", datePlusDays(1), "Two"));
        service.addAppointment(new Appointment("300", datePlusDays(1), "Three"));

        service.deleteAppointment(idToDelete);

        assertEquals(expectedSizeAfterDelete, service.getAllAppointments().size());
    }

    private static Stream<Arguments> deleteArgs() {
        return Stream.of(
            Arguments.of("200", 2),
            Arguments.of("999", 3) // deleting non-existent id should not change collection
        );
    }
}
