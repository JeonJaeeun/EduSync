import Header from "../components/Header";
import React, { useState, useRef, useCallback } from "react";
import {
    StyleSheet,
    Text,
    View,
    SafeAreaView,
    TouchableOpacity,
    TextInput,
    FlatList,
} from "react-native";
import { StatusBar } from "expo-status-bar";
import { BottomSheetModal, BottomSheetModalProvider } from "@gorhom/bottom-sheet";
import { GestureHandlerRootView } from "react-native-gesture-handler";
import { LineChart } from "react-native-chart-kit";

const StudentGradeScreen = ({ route }) => {
    const { student } = route.params;



    return (
        <GestureHandlerRootView style={{ flex: 1 }}>
            <BottomSheetModalProvider>
                <SafeAreaView style={styles.container}>
                    <StatusBar style="auto" />
                    <Header />
                    <View style={styles.content}>
                        <View style={styles.headerRow}>
                            <Text style={styles.studentName}>{student.title}</Text>
                        </View>
                        <View style={styles.gradeGragh}>
                            <Text style={styles.gradeGraghText}>성적 그래프</Text>
                        </View>
                    </View>
                </SafeAreaView>
            </BottomSheetModalProvider>
        </GestureHandlerRootView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#fff",
    },
    content: {
        flex: 1,
        padding: 20,
        backgroundColor: '#F5F5F7',
    },
    studentName: {
        fontSize: 24,
        fontWeight: "bold",
    },
    headerRow: {
        flexDirection: "row",
    },
    examTypeButton: {
        flexDirection: 'row',
        paddingLeft: 10,
        paddingTop: 15,
    },
    examTypeButtonText: {
        fontWeight: 'bold',
        fontSize: 13,
    },
    examTypeButtonAddText: {
        color: 'lightgray',
        fontWeight: 'bold',
        paddingLeft: 2,
        fontSize: 13,
    },
    gradeGragh: {
        paddingTop: 10,
    },
    gradeGraghText: {
        fontWeight: 'bold',
    },

});

export default StudentGradeScreen;