import Header from "../components/Header";
import { useNavigation } from "@react-navigation/native";
import React, { useState, useRef } from "react";
import {
  StyleSheet,
  Text,
  View,
  SafeAreaView,
  TouchableOpacity,
  Dimensions,
} from "react-native";
import { StatusBar } from "expo-status-bar";
import Swipeable from 'react-native-gesture-handler/Swipeable';
import { BottomSheetModal, BottomSheetModalProvider } from "@gorhom/bottom-sheet";
import { GestureHandlerRootView } from "react-native-gesture-handler";
import StudentGradeScreen from "./StudentGradePage";

const StudentManagementScreen = ({ navigation }) => {

  const [cards, setCards] = useState([
    { id: 1, title: "윤태원 학생" },
    { id: 2, title: "조찬익 학생" },
    { id: 3, title: "김정우 학생" },
  ]);
  const [selectedCard, setSelectedCard] = useState(null);
  const bottomSheetRef = useRef(null);
  const swipeableRef = useRef(null);

  const openBottomSheet = (id) => {
    setSelectedCard(id);
    bottomSheetRef.current?.present();
  };

  const handleDelete = () => {
    setCards(cards.filter((card) => card.id !== selectedCard));
    bottomSheetRef.current?.dismiss();
  };

  const renderRightActions = (id) => (
    <TouchableOpacity
      style={styles.deleteButton}
      onPress={() => {
        openBottomSheet(id);
        swipeableRef.current?.close();
      }}
    >
      <Text style={styles.deleteButtonText}>삭제</Text>
    </TouchableOpacity>
  );

  return (
    <GestureHandlerRootView style={{ flex: 1 }}>
      <BottomSheetModalProvider>
        <SafeAreaView style={styles.container}>
          <StatusBar style="auto" />
          <Header />
          <View style={styles.parent}>
            <View style={styles.home}>
              {cards.map((card) => (
                <Swipeable
                  key={card.id}
                  ref={swipeableRef}
                  renderRightActions={() => renderRightActions(card.id)}
                  rightThreshold={40}
                >
                  <TouchableOpacity
                    style={styles.studentCard}
                    onPress={() => navigation.navigate("studentGrade", { student: card })}
                    >
                  
                    <Text style={styles.studentCardNameText}>{card.title}</Text>
                    <Text style={styles.studentCardInformationText}>에듀고등학교 | 고1 | 서울대학교 목표</Text>
                  </TouchableOpacity>
                </Swipeable>
              ))}

              {/* BottomSheetModal */}
              <BottomSheetModal
                ref={bottomSheetRef}
                index={0}
                snapPoints={["25%"]}
                enablePanDownToClose={true}
                backdropComponent={({ animatedIndex, style }) => (
                  <View style={[style, { backgroundColor: "rgba(0, 0, 0, 0.5)" }]} />
                )}
              >
                <View style={styles.bottomSheetContent}>
                  <Text style={styles.bottomSheetDeleteText}>한 번 삭제하면 복구할 방법이 없습니다.</Text>
                  <Text style={styles.bottomSheetRealDeleteText}>정말 삭제하시겠습니까?</Text>
                  <View style={styles.bottomSheetButtonContainer}>
                    <TouchableOpacity
                      style={[styles.bottomSheetButton, styles.deleteRealButton]}
                      onPress={handleDelete}
                    >
                      <Text style={styles.deleteButtonText}>삭제하기</Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                      style={[styles.bottomSheetButton, styles.cancelButton]}
                      onPress={() => bottomSheetRef.current?.dismiss()}
                    >
                      <Text style={styles.cancelButtonText}>취소하기</Text>
                    </TouchableOpacity>
                  </View>
                </View>
              </BottomSheetModal>
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
    padding: 20,
  },
  studentCard: {
    backgroundColor: "#FFFFFF",
    paddingTop: 15,
    paddingLeft: 15,
    paddingBottom: 40,
    borderRadius: 10,
    marginBottom: 10,
  },
  studentCardNameText: {
    fontSize: 15  ,
    fontWeight: "bold",
  },
  studentCardInformationText: {
    fontSize: 14  ,
    paddingTop: 5,
    color: '#495057'
  },
  deleteButton: {
    backgroundColor: "#FF3B30",
    justifyContent: "center",
    alignItems: "center",
    width: 60,
    height: '91%',
    borderRadius: 10,
  },
  deleteButtonText: {
    color: "white",
    fontWeight: "bold",
  },
  bottomSheetContent: {
    padding: 20,
  },
  bottomSheetDeleteText: {
    fontSize: 17,
    paddingBottom: 10,
    fontWeight: 'bold'
  },
  bottomSheetRealDeleteText: {
    fontSize: 15,
    paddingBottom: 20,
  },
  bottomSheetButtonContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    width: "100%",
  },
  bottomSheetButton: {
    padding: 10,
    borderRadius: 5,
    width: "45%",
    alignItems: "center",
    justifyContent: "center"
  },
  cancelButton: {
    backgroundColor: "#E3EEFD",
    height: 40,
    borderRadius: 10,
  },
  deleteRealButton: {
    backgroundColor: "#FF3B30",
    height: 40,
    borderRadius: 10,
  },
  deleteButtonText: {
    color: "#ffffff",
    fontWeight: "bold",
  },
  cancelButtonText: {
    color: '#0064ff',
    fontWeight: "bold"
  }
});

export default StudentManagementScreen;