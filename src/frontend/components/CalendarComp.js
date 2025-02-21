import { View, StyleSheet } from "react-native";
import { Calendar, LocaleConfig } from "react-native-calendars";

const CalendarComponent = ({ selectedDate, setSelectedDate }) => {
  const markedDates = {
    [selectedDate]: { selected: true, selectedColor: '#0064ff' },
  };

  LocaleConfig.locales.fr = {
    monthNames: [
      '01월', '02월', '03월', '04월', '05월', '06월', '07월', '08월', '09월', '10월', '11월', '12월',
    ],
    monthNamesShort: [
      '01월', '02월', '03월', '04월', '05월', '06월', '07월', '08월', '09월', '10월', '11월', '12월',
    ],
    dayNames: [
      '일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일',
    ],
    dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
    today: "Aujourd'hui",
  };

  LocaleConfig.defaultLocale = 'fr';

  return (
      <Calendar
        style={styles.calendarbox}
        onDayPress={(day) => setSelectedDate(day.dateString)}
        markedDates={markedDates}
        monthFormat={'yyyy MM월'}
        hideExtraDays={true}
        firstDay={7}
        theme={{
          'stylesheet.calendar.header': {
            dayTextAtIndex0: {
              color: '#FF0000',
            },
            dayTextAtIndex6: {
              color: '#007BA4',
            },
          },
          todayTextColor: '#5484F6',
          arrowColor: '#5484F6',
          textDayFontWeight: 'bold',
          textMonthFontWeight: 'bold',
        }}
      />
  );
};

const styles = StyleSheet.create({
  calendarbox: {
    width: 350,
  },
});

export default CalendarComponent;