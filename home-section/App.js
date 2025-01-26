import React, { useState, useRef, useMemo } from 'react';
import { StyleSheet, Text, View, Button, SafeAreaView, TouchableOpacity, TextInput } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import  BottomSheet from "@gorhom/bottom-sheet";
//import { Calendar } from "react-native-calendars";
import { GestureHandlerRootView } from 'react-native-gesture-handler';

export default function App() {
  // today
  const currentDate = new Date();
  let month = currentDate.getMonth() + 1;
  let date = currentDate.getDate();
  const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
  const dayOfWeek = daysOfWeek[currentDate.getDay()];
  //bottomSheet
  const bottomSheetRef = useRef(null);
  const snapPoints = useMemo(() => ['25%', '50%', '100%'], []);
  const openBottomSheet = () => {
    if (bottomSheetRef.current) {
      bottomSheetRef.current.expand(); // 
    }
  };

  


  return (
    <GestureHandlerRootView style={{ flex: 1 }}> 
        <SafeAreaView style={styles.container}>
          <StatusBar style="auto" />
          <View style={styles.parent}>
            <View style={styles.header}></View> {/*header */}
            <View style={styles.home}>
              <View style={styles.calendarArea}> {/* calendar */}
                <View style={{ flexDirection: "row" }}>
                  <Text style={styles.calendar}>캘린더</Text>
                  <TouchableOpacity
                    style={styles.openButton}
                    onPress={openBottomSheet}
                  >
                    <Text style={styles.buttonText}>{'>'}</Text>
                  </TouchableOpacity>

                  {/* Bottom Sheet  */}
                  <BottomSheet
                      ref={bottomSheetRef}
                      snapPoints={snapPoints}
                      index={0}  
                      enablePanDownToClose={true}
                      
                    
                  >       
                    <View style={styles.bottomSheetContent}>
                      <Text style={styles.bottomSheetText}>캘린더 구현</Text>
                    </View>
                    </BottomSheet>
                </View>
                <View style={{ flexDirection: "row" }}>
                  <Text style={styles.todaymonthdate}>{month}.{date}</Text>
                  <Text style={styles.todaydayofweek}>{dayOfWeek}</Text>
                  <Text style={styles.bar}>|</Text>
                  <Text style={styles.todaySchdule}>pm 18:00 윤태원 학생</Text>
                </View>
                <Text style={styles.calendar7days}>일주일 단위 구현</Text>
              </View>
              <View style={styles.classjournalArea}>
                <Text style={{ fontWeight: "bold", fontSize: 18 }}>수업일지</Text>
              </View> {/* 수업일지*/}
            </View>
          </View>
        </SafeAreaView>
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
    backgroundColor: "#e8e9eb",
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
    backgroundColor: "#78a9ed",
    padding: 15,
    borderRadius: 50,
    justifyContent: 'center',
    alignItems: "center",
    marginTop: 25,
    marginLeft: 1,
    paddingHorizontal: 6,
    paddingVertical: 1,
  },
  buttonText: {
    color: "#0064ff",
    fontSize: 15,
    fontWeight: "bold",
  },

  bottomSheetContent: {
    //flex: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  bottomSheetText: {
    fontSize: 18,
    
  },

});
