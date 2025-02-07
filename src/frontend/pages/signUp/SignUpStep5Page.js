// 학교

import React, { useState } from "react";
import {
    View,
    Text,
    TextInput,
    TouchableOpacity,
    StyleSheet,
    KeyboardAvoidingView,
    ScrollView,
    Platform,
} from "react-native";
import { useNavigation } from "@react-navigation/native";
import SignUpHeader from "../../components/SignUpHeader";

export default function SignUpStep5Page({ route }) {
    const navigation = useNavigation();
    const { email, password, nickname, phone, userTypes } = route.params; // Step4에서 받은 정보

    const [schoolName, setSchoolName] = useState("");
    const [isContinueEnabled, setIsContinueEnabled] = useState(false);

    const handleSchoolChange = (text) => {
        setSchoolName(text);
        setIsContinueEnabled(text.length > 0);
    };

    const handleContinue = () => {
        navigation.navigate("SignupStep6", {
            email,
            password,
            nickname,
            phone,
            userTypes,
            schoolName,
        });
    };

    return (
        <KeyboardAvoidingView
            style={styles.container}
            behavior={Platform.OS === "ios" ? "padding" : undefined}
        >
            <ScrollView
                contentContainerStyle={styles.scrollContainer}
                keyboardShouldPersistTaps="handled"
            >
                <SignUpHeader
                    title="내 손 안에 과외수업!"
                    subtitle="앱 내 정보를 추가입력 해보아요!"
                />

                <View style={styles.inputContainer}>
                    <Text style={styles.label}>학교명</Text>
                    <TextInput
                        style={styles.input}
                        placeholder="학교명을 입력해주세요"
                        placeholderTextColor="#BDBDBD"
                        value={schoolName}
                        onChangeText={handleSchoolChange}
                    />
                </View>
            </ScrollView>

            <View style={styles.buttonContainer}>
                <TouchableOpacity
                    style={[styles.nextButton, !isContinueEnabled && styles.disabledButton]}
                    disabled={!isContinueEnabled}
                    onPress={handleContinue}
                >
                    <Text style={styles.nextButtonText}>계속하기</Text>
                </TouchableOpacity>
            </View>
        </KeyboardAvoidingView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#FFF",
    },
    scrollContainer: {
        paddingHorizontal: 20,
        paddingTop: 80,
        flexGrow: 1,
    },
    inputContainer: {
        marginTop: 20,
    },
    label: {
        fontSize: 16,
        fontWeight: "bold",
        color: "#333",
        marginBottom: 5,
    },
    input: {
        height: 50,
        borderBottomWidth: 1,
        borderBottomColor: "#ddd",
        fontSize: 16,
        paddingHorizontal: 10,
        marginBottom: 10,
    },
    buttonContainer: {
        paddingHorizontal: 20,
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