
import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class CalendarPanel {

    public static JPanel createCalendarPanel() {
        JPanel calendarPanel = new JPanel(new GridLayout(7, 7));

        // Create an instance of the calendar
        Calendar calendar = Calendar.getInstance();

        // Set the current date
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Set the calendar's date to the current date
        calendar.set(currentYear, currentMonth, 1);

        // Add the days of the week as labels
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            calendarPanel.add(label);
        }

        // Add empty labels for preceding days
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i < firstDayOfWeek; i++) {
            JLabel label = new JLabel("");
            calendarPanel.add(label);
        }

        // Add labels for each day of the month
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= daysInMonth; day++) {
            JLabel label = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            if (day == currentDay) {
                label.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
            calendarPanel.add(label);
        }

        return calendarPanel;
    }
    
}
