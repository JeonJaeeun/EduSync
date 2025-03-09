// 사용자 유형

import React, { useState } from "react";
import { View, TouchableOpacity, StyleSheet, Text, Modal, TouchableWithoutFeedback } from "react-native";
import { useNavigation } from "@react-navigation/native";
import SignUpHeader from "../../components/SignUpHeader";
import { AntDesign } from "@expo/vector-icons";

export default function SignUpStep1Page() {
    const [modalVisible, setModalVisible] = useState(false);
    const [selectedUserType, setSelectedUserType] = useState("");
    const navigation = useNavigation();

    const userType = ["학생", "학부모", "과외 선생님"];

    const handleSelectUserType = (type) => {
        setSelectedUserType(type);
        setModalVisible(false);
    };

    const handleContinue =() => {
        if(selectedUserType){
            navigation.navigate("SignupStep2", { userType: selectedUserType });
        }
    };

    return (
        <View style={styles.container}>
            <SignUpHeader
                title="내 손안에 과외수업!"
                subtitle="학생, 선생님, 학부모 중 선택해주세요!"
            />

            <View style={styles.selectionContainer}>
                <Text style={styles.placeholder}>어떤 유형의 사용자이신가요? :)</Text>
                <Text style={styles.label}>사용자 유형</Text>
                <TouchableOpacity style={styles.selectionBox} onPress={() => setModalVisible(true)}>
                    <Text style={[styles.selectionText, !selectedUserType && styles.placeholderText]}>
                        {selectedUserType || "선택"}
                    </Text>
                    <AntDesign name="down" size={16} color="#999" />
                </TouchableOpacity>
            </View>

            <Modal visible={modalVisible} transparent animationType="fade">
                <View style={styles.modalOverlay}>
                    <TouchableWithoutFeedback onPress={() => setModalVisible(false)}>
                        <View style={styles.modalBackground} />
                    </TouchableWithoutFeedback>

                    <View style={styles.modalContent}>
                        <View style={styles.modalHandle} />
                        <Text style={styles.modalTitle}>사용자 유형을 선택해주세요</Text>
                        {userType.map((type, index) => (
                            <TouchableOpacity
                                key={index}
                                style={styles.option}
                                onPress={() => handleSelectUserType(type)}
                            >
                                <Text style={[
                                    styles.optionText,
                                    selectedUserType === type && styles.selectedOptionText
                                ]}>
                                    {type}
                                </Text>
                                {selectedUserType === type && <AntDesign name="check" size={16} color="#007AFF" />}
                            </TouchableOpacity>
                        ))}
                    </View>
                </View>
            </Modal>

            {/* 계속하기 버튼 */}
            <View style={styles.bottomContainer}>
                <TouchableOpacity
                    style={[styles.nextButton, !selectedUserType && styles.disabledButton]}
                    disabled={!selectedUserType}
                    onPress={handleContinue}
                >
                    <Text style={styles.nextButtonText}>계속하기</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#FFF",
        paddingHorizontal: 20,
        paddingTop: 80,
    },
    selectionContainer: {
        marginTop: 20,
    },
    label: {
        fontSize: 16,
        fontWeight: "bold",
        color: "#333",
    },
    placeholder: {
        fontSize: 14,
        color: "#999",
        marginBottom: 15,
    },
    selectionBox: {
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
        paddingVertical: 15,
        paddingHorizontal: 15,
        borderBottomWidth: 1,
        borderColor: "#ddd",
        marginTop: 10,
    },
    selectionText: {
        fontSize: 16,
        color: "#333",
    },
    placeholderText: {
        color: "#999",
    },
    modalOverlay: {
        flex: 1,
        justifyContent: "flex-end",
        backgroundColor: "rgba(0, 0, 0, 0.5)",
    },
    modalBackground: {
        flex: 1,
    },
    modalContent: {
        backgroundColor: "#fff",
        paddingVertical: 20,
        paddingHorizontal: 25,
        borderTopLeftRadius: 15,
        borderTopRightRadius: 15,
    },
    modalHandle: {
        width: 40,
        height: 5,
        backgroundColor: "#ccc",
        borderRadius: 10,
        alignSelf: "center",
        marginBottom: 20,
    },
    modalTitle: {
        fontSize: 18,
        fontWeight: "bold",
        marginBottom: 15,
    },
    option: {
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
        paddingVertical: 15,
    },
    optionText: {
        fontSize: 16,
        color: "#666",
    },
    selectedOptionText: {
        color: "#000",
        fontWeight: "bold",
    },
    bottomContainer: {
        flex: 1,
        justifyContent: "flex-end",
        paddingBottom: 50,
    },
    nextButton: {
        backgroundColor: "#007AFF",
        paddingVertical: 15,
        borderRadius: 10,
        alignItems: "center",
    },
    disabledButton: {
        backgroundColor: "#ccc",
    },
    nextButtonText: {
        color: "#FFF",
        fontSize: 16,
        fontWeight: "bold",
    },
});
