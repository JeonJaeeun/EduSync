// 닉네임, 휴대폰 번호

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

export default function SignUpStep4Page({ route }) {
    const navigation = useNavigation();
    const { userType, email, password } = route.params;

    const [nickname, setNickname] = useState("");
    const [phone, setPhone] = useState("");
    const [isContinueEnabled, setIsContinueEnabled] = useState(false);
    const [step, setStep] = useState(1);
    const [nicknameValid, setNicknameValid] = useState(false);
    const [isFirstSkipVisible, setIsFirstSkipVisible] = useState(true);
    const [isSecondSkipVisible, setIsSecondSkipVisible] = useState(false);

    const handleNicknameChange = (text) => {
        setNickname(text);
        setIsContinueEnabled(text.length > 0);
    };

    const handleContinue = () => {
        if (step === 1) {
            setNicknameValid(true);
            setStep(2);
            setIsContinueEnabled(false);
            setIsFirstSkipVisible(false);
            setIsSecondSkipVisible(true);
        } else {
            if(userType === "학생") {
                navigation.navigate("SignupStep5", {email, password, nickname, phone, userType});
            } else {
                navigation.navigate("SignupStep6", {email, password, nickname, phone, userType});
            }
        }
    };

    const handleSkip = () => {
        setStep(2);
        setIsFirstSkipVisible(false);
        setIsSecondSkipVisible(true);
    };

    const handlePhoneChange = (text) => {
        setPhone(text);
        setIsContinueEnabled(text.length > 0);
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
                    title="내 손안에 과외수업!"
                    subtitle="앱 내 정보를 추가입력 해보아요!"
                />

                <View style={styles.inputContainer}>
                    <Text style={styles.label}>닉네임 (선택)</Text>
                    <TextInput
                        style={styles.input}
                        placeholder="닉네임을 입력해주세요"
                        placeholderTextColor="#BDBDBD"
                        value={nickname}
                        onChangeText={handleNicknameChange}
                    />
                    {nicknameValid && <Text style={styles.validText}>사용가능한 닉네임입니다</Text>}
                </View>

                {isFirstSkipVisible && (
                    <TouchableOpacity onPress={handleSkip} style={styles.skipButton}>
                        <Text style={styles.skipText}>건너뛰기 &gt;</Text>
                    </TouchableOpacity>
                )}

                {step === 2 && (
                    <View style={styles.inputContainer}>
                        <Text style={styles.label}>휴대폰 번호 (선택)</Text>
                        <TextInput
                            style={styles.input}
                            placeholder="전화번호 입력"
                            placeholderTextColor="#BDBDBD"
                            keyboardType="numeric"
                            value={phone}
                            onChangeText={handlePhoneChange}
                        />
                        {isSecondSkipVisible && (
                            <TouchableOpacity onPress={() => setIsContinueEnabled(true)} style={styles.skipButton}>
                                <Text style={styles.skipText}>건너뛰기 &gt;</Text>
                            </TouchableOpacity>
                        )}
                    </View>
                )}
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
    validText: {
        color: "#007AFF",
        fontSize: 12,
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
    skipButton: {
        alignSelf: "flex-end",
        marginTop: 4,
        marginRight: 10,
    },
    skipText: {
        fontSize: 13,
        color: "#A9A9A9",
    },
});
