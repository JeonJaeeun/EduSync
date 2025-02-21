import Header from "../components/Header";
import React, { useState, useRef, useEffect } from "react";
import {
  StyleSheet,
  Text,
  View,
  SafeAreaView,
  TouchableOpacity,
  Dimensions,
  TextInput
} from "react-native";
import { StatusBar } from "expo-status-bar";
import { BottomSheetModal, BottomSheetModalProvider } from "@gorhom/bottom-sheet";
import { GestureHandlerRootView } from "react-native-gesture-handler";
import DateTimePicker from "@react-native-community/datetimepicker";
import CalendarComponent from "../components/CalendarComp";

export default function HomeScreen() {
  const currentDate = new Date();
  let year = currentDate.getFullYear();
  let month = currentDate.getMonth() + 1;
  let date = currentDate.getDate();
  const daysOfWeek = ["월", "화", "수", "목", "금", "토", "일",];
  const dayOfWeek = daysOfWeek[(currentDate.getDay() + 6) % 7];

  const bottomSheetRef = useRef(null);
  const openBottomSheet = () => {
    if (bottomSheetRef.current) {
      bottomSheetRef.current?.present();
    }
    setSelectedDate(today);
    setIsModalVisible(true);
    setShowInput(false);
    setSnapPoints(["60%", "90%"]);
  };

  const today = new Date();
  const todayString = `${currentDate.getFullYear()}-${String(currentDate.getMonth() + 1).padStart(2, '0')}-${String(currentDate.getDate()).padStart(2, '0')}`;

  const [selectedDate, setSelectedDate] = useState(todayString);
  const markedDates = {
    [selectedDate]: { selected: true, selectedColor: '#0064ff' },
  };

  const [isModalVisible, setIsModalVisible] = useState(false);
  const [showAddButton, setShowAddButton] = useState(true);
  const [snapPoints, setSnapPoints] = useState(["60%", "90%"]);
  const [showInput, setShowInput] = useState(false);
  const [selectedTime, setSelectedTime] = useState(new Date());
  const [scheduleText, setScheduleText] = useState("");
  const [schedules, setSchedules] = useState({});

  const onTimeChange = (event, selected) => {
    setSelectedTime(selected || selectedTime);
  };

  const [selectedMonth, setSelectedMonth] = useState(month);
  const [selectedDay, setSelectedDay] = useState(date);
  const [selectedDayOfWeek, setSelectedDayOfWeek] = useState(dayOfWeek);

  const handleDateSelect = (dateString) => {
    const selected = new Date(dateString);
    const localDate = new Date(
      selected.getFullYear(),
      selected.getMonth(),
      selected.getDate()
    );
  
    setSelectedDate(dateString);
    setSelectedMonth(localDate.getMonth() + 1);
    setSelectedDay(localDate.getDate());
    setSelectedDayOfWeek(daysOfWeek[(localDate.getDay() + 6) % 7]); // 인덱스 보정 강화
  };

  const deleteSchedule = (date, id) => {
    setSchedules(prev => {
      const updatedSchedules = prev[date]?.filter(schedule => schedule.id !== id);
      if (!updatedSchedules || updatedSchedules.length === 0) {
        const newSchedules = { ...prev };
        delete newSchedules[date];
        return newSchedules;
      }
      return { ...prev, [date]: updatedSchedules };
    });
  };

  const saveSchedule = () => {
    if (scheduleText.trim() !== "") {
      setSchedules(prev => ({
        ...prev,
        [selectedDate]: [...(prev[selectedDate] || []), {
          id: Date.now(),
          time: selectedTime.toLocaleTimeString("ko-KR", { hour: '2-digit', minute: '2-digit', hour12: false }),
          text: scheduleText
        }]
      }));
      setScheduleText("");
      setShowInput(false);
      setSnapPoints(["30%", "90%"]);
    }
  };

  useEffect(() => {
    if (showInput) {
      setSnapPoints(["90%"]);
    } else {
      setSnapPoints(["60%", "80%"]);
    }
  }, [showInput]);

  useEffect(() => {
    const today = new Date();
    const todayString = getLocalDateString(today);
    handleDateSelect(todayString);
  }, []);



  // Horizontal Calendar 
  const getWeekDates = () => {
    const dates = [];
    const today = new Date();
    const startOfWeek = new Date(today);
    
    // 월요일 기준 주 시작 계산
    const day = today.getDay(); // 0:일 ~ 6:토
    const diff = day === 0 ? 6 : day - 1; // 일요일인 경우 6일 전으로
    startOfWeek.setDate(today.getDate() - diff);
  
    for (let i = 0; i < 7; i++) {
      const date = new Date(startOfWeek);
      date.setDate(startOfWeek.getDate() + i);
      dates.push(date);
    }
    return dates;
  };

  const getLocalDateString = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  };

  const renderHorizontalCalendar = () => {
    const weekDates = getWeekDates();
    return (
      <View style={styles.horizontalCalendarContainer}>
        {weekDates.map((item, index) => {
          const localDateString = getLocalDateString(item);
          const dayOfMonth = item.getDate();
          const adjustedDayIndex = (item.getDay() + 6) % 7;
          const dayOfWeek = daysOfWeek[adjustedDayIndex];
          const isToday = localDateString === getLocalDateString(new Date());
          const isSunday = item.getDay() === 0;
          const isSaturday = item.getDay() === 6;
  
          return (
            <TouchableOpacity
              key={index}
              style={[
                styles.horizontalCalendarItem,
                isToday && styles.todayItem,
                localDateString === selectedDate && styles.selectedDateItem
              ]}
              onPress={() => handleDateSelect(localDateString)}
            >
              <Text style={[
                styles.horizontalCalendarDay,
                isSunday && styles.sundayText,
                isSaturday && styles.saturdayText,
                isToday && styles.todayText,
                localDateString === selectedDate && styles.selectedDateText
              ]}>
                {dayOfWeek}
              </Text>
              <Text style={[
                styles.horizontalCalendarDate,
                isSunday && styles.sundayText,
                isSaturday && styles.saturdayText,
                isToday && styles.todayText,
                localDateString === selectedDate && styles.selectedDateText
              ]}>
                {dayOfMonth}
              </Text>
            </TouchableOpacity>
          );
        })}
      </View>
    );
  };

  return (
    <GestureHandlerRootView style={{ flex: 1 }}>
      <BottomSheetModalProvider>
        <SafeAreaView style={styles.container}>
          <StatusBar style="auto" />
          <Header />
          <View style={styles.parent}>
            <View style={styles.home}>
              <View style={styles.calendarArea}>
                <View style={{ flexDirection: "row" }}>
                  <Text style={styles.calendar}>캘린더</Text>
                  <TouchableOpacity
                    style={styles.openButton}
                    onPress={openBottomSheet}
                  >
                    <Text style={styles.buttonText}>{">"}</Text>
                  </TouchableOpacity>

                  <BottomSheetModal
                    ref={bottomSheetRef}
                    snapPoints={snapPoints}
                    index={0}
                    enablePanDownToClose={true}
                    backdropComponent={({ animatedIndex, style }) => (
                      <View style={[style, { backgroundColor: 'rgba(0, 0, 0, 0.5)' }]} />
                    )}
                  >
                    <View style={styles.bottomSheetContent}>
                      <CalendarComponent
                        selectedDate={selectedDate}
                        setSelectedDate={setSelectedDate}
                        schedules={schedules}
                        deleteSchedule={deleteSchedule}
                      />
                      <View style={styles.calendarLine} />
                      {schedules[selectedDate]?.map((sch) => (
                        <View key={sch.id} style={styles.scheduleItem}>
                          <Text style={styles.scheduleTimeStyle}>{sch.time}</Text>
                          <Text style={styles.scheduleTextStyle}>{sch.text}</Text>
                          <TouchableOpacity onPress={() => deleteSchedule(selectedDate, sch.id)}>
                            <Text style={styles.deleteButton}>X</Text>
                          </TouchableOpacity>
                        </View>
                      ))}
                      {!showInput && (
                        <TouchableOpacity
                          style={styles.addScheduleButton}
                          onPress={() => {
                            setShowInput(true);
                          }}
                        >
                          <Text style={styles.addScheduleButtonText}>+ 일정 추가하기</Text>
                        </TouchableOpacity>
                      )}

                      {showInput && (
                        <View style={styles.scheduleInputContainer}>
                          <DateTimePicker value={selectedTime} mode="time" is24Hour={true} display="default" onChange={onTimeChange} />
                          <TextInput
                            style={styles.scheduleInput}
                            placeholder="일정을 입력하세요"
                            onChangeText={setScheduleText}
                            autoCorrect={false}
                            autoCapitalize="none"
                          />
                          <TouchableOpacity style={styles.saveScheduleButton} onPress={saveSchedule}>
                            <Text style={styles.saveScheduleButtonText}>저장</Text>
                          </TouchableOpacity>
                        </View>
                      )}
                    </View>
                  </BottomSheetModal>
                </View>
                <View style={{ flexDirection: "row" }}>
                  <View style={{ flex: 1, flexDirection: "row" }}>
                    <Text style={styles.todaymonthdate}>{selectedMonth}.{selectedDay}</Text>
                    <Text style={styles.todaydayofweek}>{selectedDayOfWeek}</Text>
                  </View>
                  <View style={{ flex: 1, flexDirection: "row" }}>
                    <View style={styles.verticaline} />
                    <Text style={styles.todaySchdule}>기록된 일정이 없습니다.</Text>
                  </View>
                </View>
                {/* Horizontal Calendar 추가 */}
                {renderHorizontalCalendar()}
              </View>
              <View style={styles.classjournalArea}>
                <Text style={{ fontWeight: 'bold', fontSize: 20 }}>수업일지</Text>
                {/*<TouchableOpacity
                    style={styles.classjournalViewButton}
                    //onPress={} 전체보기 클릭시 최신 수업일지
                  >
                    <Text style={{color: 'gray', fontSize: 13}}>{"전체보기 〉"}</Text>
                  </TouchableOpacity> */}
                <View style={styles.journalContainer}>
                  {[3, 2, 1].map((item) => (
                    <View key={item} style={styles.journalBox}>
                      <View style={styles.journalBoxTitle} >
                        <Text style={styles.journalWeekText}>{item}주차 수업일지</Text>
                        <Text style={styles.journalStudentNameText}>김에듀 학생</Text>
                      </View>
                      <View style={styles.journalContent}>
                        <Text style={styles.journalContentText}>수학</Text>
                        <Text style={styles.journalContentText}>미적분_수열의 극한</Text>
                        <Text style={styles.journalContentText}>1.수열의 극한....</Text>
                      </View>
                      <TouchableOpacity
                        style={styles.editButton}
                      // onPress={() => }
                      >
                        <Text style={styles.editButtonText}>일지수정</Text>
                      </TouchableOpacity>
                    </View>
                  ))}
                </View>
              </View>
            </View>
          </View>
        </SafeAreaView>
      </BottomSheetModalProvider>
    </GestureHandlerRootView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
  },
  parent: {
    flex: 1,
  },
  home: {
    flex: 4.46,
    backgroundColor: "#F5F5F7",
  },
  calendarArea: {
    flex: 1,
  },
  classjournalArea: {
    paddingLeft: 20,
    paddingBottom: 370,
  },
  journalContainer: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'space-between',
    marginTop: 10,
  },
  journalBox: {
    width: 335,
    height: 105,
    backgroundColor: '#FFFFFF',
    borderRadius: 10,
    marginTop: 13,
  },
  journalBoxTitle: {
    flexDirection: 'row',
  },
  journalWeekText: {
    fontSize: 14,
    paddingTop: 15,
    paddingLeft: 15,
    paddingBottom: 8,
    fontWeight: 'bold'
  },
  journalStudentNameText: {
    paddingLeft: 7,
    paddingTop: 17,
    fontSize: 12,
    color: 'gray'
  },
  journalContent: {
    paddingLeft: 15,
    flex: 1,
  },
  journalContentText: {
    fontSize: 13,
    color: '#495057',
  },

  editButton: {
    backgroundColor: '#5484F6',
    borderRadius: 10,
    paddingTop: 7,
    paddingLeft: 7,
    marginLeft: 270,
    marginRight: 10,
    marginBottom: 10,
    marginTop: 6,
    flex: 1,
  },
  editButtonText: {
    color: '#ffffff',
    fontSize: 10.5,
  },
  classjournalViewButton: {
    fontSize: 20,
    paddingLeft: 200,
    height: 20,
    justifyContent: 'flex-end'
  },
  calendar: {
    fontWeight: "bold",
    fontSize: 16,
    marginTop: 27,
    marginLeft: 16,
  },
  todaymonthdate: {
    fontWeight: "bold",
    fontSize: 35,
    marginTop: 10,
    marginLeft: 20,
  },
  todaydayofweek: {
    fontWeight: "bold",
    fontSize: 15,
    marginTop: 30,
    marginLeft: 1,
  },
  todaySchdule: {
    marginLeft: 5,
    marginTop: 10,
  },
  calendar7days: {
    marginLeft: 20,
    marginTop: 10,
  },
  openButton: {
    backgroundColor: "#E3EEFD",
    padding: 15,
    borderRadius: 50,
    justifyContent: "center",
    alignItems: "center",
    marginTop: 25,
    marginLeft: 1,
    paddingHorizontal: 6,
    paddingVertical: 1,
  },
  buttonText: {
    color: "#5484F6",
    fontSize: 15,
    fontWeight: "bold",
  },

  bottomSheetContent: {
    //flex: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  addScheduleButton: {
    borderRadius: 50,
    backgroundColor: "#E3EEFD",
    padding: 5,
    paddingLeft: 30,
    paddingRight: 30,
    justifyContent: "center",
    alignItems: "center",
  },
  addScheduleButtonText: {
    color: "#5484F6",
    fontSize: 12
  },
  calendarLine: {
    height: 1,
    backgroundColor: 'lightgray',
    width: 330,
    marginTop: 10,
    marginBottom: 10,

  },
  verticaline: {
    width: 3,
    backgroundColor: 'lightgray',
    marginRight: 10,
  },
  scheduleInputContainer: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: '#E3EEFD',
    borderRadius: 50,
    width: 330,
    height: 40,
  },
  scheduleInput: {
    borderWidth: 0,
    padding: 5,
    marginHorizontal: 10,
    flex: 1,
    fontSize: 14,
  },
  deleteButton: {
    fontSize: 15,
    color: "#5484F6",
    backgroundColor: '#ffffff',
    marginLeft: 10,
    borderRadius: 50,
    paddingLeft: 5,
    paddingRight: 5,
  },
  scheduleItem: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    borderRadius: 50,
    backgroundColor: "#E3EEFD",
    paddingLeft: 10,
    paddingRight: 10,
    paddingTop: 4,
    paddingBottom: 4,
    marginBottom: 10,
    width: 330,
  },
  scheduleTimeStyle: {
    backgroundColor: '#5484F6',
    borderRadius: 50,
    padding: 5,
    color: '#ffffff',
    marginTop: 2,
    marginBottom: 2,
    marginLeft: 2,
    fontSize: 12,
  },
  scheduleTextStyle: {
    width: 200,
    fontSize: 14,
  },
  saveScheduleButton: {
    backgroundColor: '#ffffff',
    marginRight: 10,
    borderRadius: 50,
  },
  saveScheduleButtonText: {
    color: '#5484F6',
    padding: 5,
  },
  horizontalCalendarContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    paddingHorizontal: 10,
    marginTop: 15,
  },
  horizontalCalendarItem: {
    alignItems: "center",
    justifyContent: "center",
    width: Dimensions.get("window").width / 7 - 10,
    height: 75,
    borderRadius: 40,
    backgroundColor: "#FFFFFF",
    borderWidth: 1,
    borderColor: "#D3D3D3",
  },
  todayItem: {
    backgroundColor: "#E3EEFD",
  },
  horizontalCalendarDay: {
    fontSize: 14,
    fontWeight: "bold",
    color: "#000",
    paddingTop: 10,
    paddingBottom: 7,
  },
  horizontalCalendarDate: {
    fontSize: 16,
    fontWeight: "bold",
    color: "#000",
    paddingBottom: 10,
  },
  sundayText: {
    color: "#FF0000",
  },
  saturdayText: {
    color: "#007BA4",
  },
  todayText: {
    color: "#5484F6",
  },
  selectedDateItem: {
    backgroundColor: '#5484F6',
  },
  selectedDateText: {
    color: '#FFFFFF',
  },


});
