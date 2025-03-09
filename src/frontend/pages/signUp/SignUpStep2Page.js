// 이메일, 이메일 인증

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
import { useNavigation, useRoute } from "@react-navigation/native";
import SignUpHeader from "../../components/SignUpHeader";

export default function SignUpStep2Page() {
    const navigation = useNavigation();
    const route = useRoute();
    const { userType } = route.params;
    const [email, setEmail] = useState("");
    const [verificationCode, setVerificationCode] = useState("");
    const [isEmailValid, setIsEmailValid] = useState(false);
    const [isCodeSent, setIsCodeSent] = useState(false);
    const [isCodeValid, setIsCodeValid] = useState(false);
    const [isVerified, setIsVerified] = useState(false);

    const handleEmailChange = (text) => {
        setEmail(text);
        setIsEmailValid(text.includes("@") && text.includes("."));
    };

    const handleCodeChange = (text) => {
        setVerificationCode(text);
        setIsCodeValid(text.length === 6);
    };

    const sendVerificationCode = () => {
        setIsCodeSent(true);
        // 실제로 서버에 요청해서 인증번호를 전송하는 로직 추가
    };

    const verifyCode = () => {
        // 인증번호 검증 로직 (서버와 통신 필요)
        if (verificationCode === "123456") { // 예제 코드
            setIsVerified(true);
            navigateToNextStep();
        } else {
            alert("인증번호가 올바르지 않습니다. 다시 확인해주세요.");
        }
    };

    const navigateToNextStep = () => {
        navigation.navigate("SignupStep3", { email, userType });
    };

    const handleContinue = () => {
        if (isCodeSent && isCodeValid) {
            verifyCode();
        } else if (!isCodeSent && isEmailValid) {
            sendVerificationCode();
        }
    };

    const isContinueEnabled = isCodeSent ? isCodeValid : isEmailValid;

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
                    subtitle="아이디로 사용할 이메일을 입력해주세요!"
                />

                <View style={styles.inputContainer}>
                    <Text style={styles.label}>이메일 아이디</Text>
                    <TextInput
                        style={[styles.input, isCodeSent && styles.disabledInput]}
                        placeholder="예) Edu_Sync@edusync.com"
                        placeholderTextColor="#BDBDBD"
                        value={email}
                        onChangeText={handleEmailChange}
                        keyboardType="email-address"
                        autoCapitalize="none"
                        editable={!isCodeSent}
                    />

                    {isCodeSent && (
                        <View style={styles.verificationNotice}>
                            <Text style={styles.noticeText}>인증번호가 발송되었습니다</Text>
                            <TouchableOpacity>
                                <Text style={styles.resendText}>인증번호 재발송</Text>
                            </TouchableOpacity>
                        </View>
                    )}
                </View>

                {isCodeSent && (
                    <View style={styles.inputContainer}>
                        <Text style={styles.label}>인증번호</Text>
                        <TextInput
                            style={styles.input}
                            placeholder="인증번호 6자리"
                            placeholderTextColor="#BDBDBD"
                            value={verificationCode}
                            onChangeText={handleCodeChange}
                            keyboardType="number-pad"
                        />
                    </View>
                )}
            </ScrollView>

            {/* 계속하기 버튼 */}
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
        marginBottom: 15,
    },
    disabledInput: {
        backgroundColor: "#f2f2f2",
        borderRadius: 8,
    },
    verificationNotice: {
        flexDirection: "row",
        alignItems: "center",
        justifyContent: "space-between",
    },
    noticeText: {
        color: "#007AFF",
        fontSize: 12,
    },
    resendText: {
        color: "#007AFF",
        fontSize: 12,
        textDecorationLine: "underline",
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