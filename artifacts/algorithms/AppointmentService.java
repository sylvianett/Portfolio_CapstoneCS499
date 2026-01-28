package appointment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private List<Appointment> appointments = new ArrayList<>();

    public void addAppointment(Appointment appointment) {
        for (Appointment a : appointments) {
            if (a.getAppointmentId().equals(appointment.getAppointmentId())) {
                throw new IllegalArgumentException("Duplicate appointment ID");
            }
        }
        appointments.add(appointment);
    }

    public void deleteAppointment(String appointmentId) {
        appointments.removeIf(a -> a.getAppointmentId().equals(appointmentId));
    }

    public List<Appointment> getAllAppointments() {
        return appointments;
    }
}
