import React, { useState, useRef, useMemo } from "react";
import {
  StyleSheet,
  Text,
  View,
  Button,
  SafeAreaView,
  TouchableOpacity,
  TextInput,
} from "react-native";
import { StatusBar } from "expo-status-bar";
import BottomSheet, { BottomSheetModal, BottomSheetModalProvider } from "@gorhom/bottom-sheet";
import { Calendar, LocaleConfig } from "react-native-calendars";
import { GestureHandlerRootView } from "react-native-gesture-handler";

export default function HomeScreen() {
  // today
  const currentDate = new Date();
  let year = currentDate.getFullYear();
  let month = currentDate.getMonth() + 1;
  let date = currentDate.getDate();
  const daysOfWeek = ["일", "월", "화", "수", "목", "금", "토"];
  const dayOfWeek = daysOfWeek[currentDate.getDay()];
  //bottomSheet
  const bottomSheetRef = useRef(null);
  const snapPoints = useMemo(() => ["60%", "90%" ], []);
  const openBottomSheet = () => {
    if (bottomSheetRef.current) {
      bottomSheetRef.current?.present(); 
    }
    setSelectedDate(today); // 버튼 누를 때마다 오늘 날짜로 초기화
    setIsModalVisible(true);
  };

    // 오늘 날짜 가져오기 (yyyy-MM-dd 형식)
    const today = `${year}-${String(month).padStart(2, '0')}-${String(date).padStart(2, '0')}`;
    // 선택된 날짜 상태 (초기값: 오늘)
    const [selectedDate, setSelectedDate] = useState(today);
    // markedDates를 이용해 선택한 날짜를 파란색 원으로 표시
    const markedDates = {
      [selectedDate]: { selected: true, selectedColor: '#0064ff' },
    };

    const [isModalVisible, setIsModalVisible] = useState(false);
    const [showAddButton, setShowAddButton] = useState(true);


  LocaleConfig.locales.fr = {
    monthNames: [
      '01월',
      '02월',
      '03월',
      '04월',
      '05월',
      '06월',
      '07월',
      '08월',
      '09월',
      '10월',
      '11월',
      '12월',
    ],
    monthNamesShort: [
      '01월',
      '02월',
      '03월',
      '04월',
      '05월',
      '06월',
      '07월',
      '08월',
      '09월',
      '10월',
      '11월',
      '12월',
    ],
    dayNames: [
      '일요일',
      '월요일',
      '화요일',
      '수요일',
      '목요일',
      '금요일',
      '토요일',
    ],
    dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
    today: "Aujourd'hui",
  };
  
  LocaleConfig.defaultLocale = 'fr';

  return (
    <GestureHandlerRootView style={{ flex: 1 }}>
      <BottomSheetModalProvider>
        <SafeAreaView style={styles.container}>
          <StatusBar style="auto" />
          <View style={styles.parent}>
            <View style={styles.header}></View>
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
                  >
                    <View style={styles.bottomSheetContent}>
                     <Calendar
                        style={[styles.calendarbox]} 
                         onDayPress={(day) => {
                           setSelectedDate(day.dateString);
                          }}
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
                     <View style={styles.calendarLine} />
                      <TouchableOpacity style={styles.addScheduleButton} onPress={console.log(`${selectedDate}에 일정 추가`)}>
                        <Text style={styles.addScheduleButtonText} >+ 일정 추가하기</Text>
                      </TouchableOpacity>
                    </View>
                  </BottomSheetModal>
                </View>
                <View style={{ flexDirection: "row" }}>
                  <Text style={styles.todaymonthdate}>
                    {month}.{date}
                  </Text>
                  <Text style={styles.todaydayofweek}>{dayOfWeek}</Text>
                  <View style={styles.verticaline} />
                  <Text style={styles.todaySchdule}>pm 18:00 윤태원 학생</Text> 
                </View>
                <Text style={styles.calendar7days}>일주일 단위 구현</Text>
              </View>
              <View style={styles.classjournalArea}>
                <Text style={{ fontWeight: "bold", fontSize: 18 }}>
                  수업일지
                </Text>
              </View>
            </View>
          </View>
        </SafeAreaView>
      </BottomSheetModalProvider>
    </GestureHandlerRootView>
  );
}

const styles = StyleSheet.create({
  //상단, 하단바
  container: {
    flex: 1,
    backgroundColor: "#fff",
  },
  // 전체 배경
  parent: {
    flex: 1,
  },
  //헤더
  header: {
    flex: 1,
    backgroundColor: "#fff",
  },
  home: {
    flex: 4.46,
    backgroundColor: "#F5F5F7",
  },
  calendarArea: {
    flex: 1,
  },
  classjournalArea: {
    //flex: 1,
    paddingLeft: 20,
    paddingBottom: 350,
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
  bar: {
    fontSize: 45,
    marginLeft: 25,
    color: "gray",
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

  calendarbox: {
      width: 350 ,
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
      marginLeft: 50, 
      marginRight: 10,
  },

});

