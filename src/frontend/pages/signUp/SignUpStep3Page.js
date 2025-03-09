// 비밀번호, 비밀번호 확인

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
import Icon from "react-native-vector-icons/MaterialCommunityIcons";

export default function SignUpStep3Page({ route }) {
    const navigation = useNavigation();
    const { email, userType } = route.params;
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [isPasswordValid, setIsPasswordValid] = useState(false);
    const [doPasswordsMatch, setDoPasswordsMatch] = useState(false);
    const [step, setStep] = useState(1);
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const validatePassword = (text) => {
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{10,}$/;
        return passwordRegex.test(text);
    };

    const handlePasswordChange = (text) => {
        setPassword(text);
        setIsPasswordValid(validatePassword(text));
    };

    const handleConfirmPasswordChange = (text) => {
        setConfirmPassword(text);
        setDoPasswordsMatch(text === password);
    };

    const handleContinue = () => {
        if (step === 1 && isPasswordValid) {
            setStep(2);
        } else if (step === 2 && doPasswordsMatch) {
            navigation.navigate("SignupStep4", { email, password, userType });
        }
    };

    return (
        <KeyboardAvoidingView
            style={styles.container}
            behavior={Platform.OS === "ios" ? "padding" : undefined}
        >
            <ScrollView contentContainerStyle={styles.scrollContainer} keyboardShouldPersistTaps="handled">
                <SignUpHeader
                    title="내 손안에 과외수업!"
                    subtitle="본인만이 아는 비밀번호를 입력해주세요!"
                />

                <View style={styles.inputContainer}>
                    <Text style={styles.label}>비밀번호</Text>
                    <View style={styles.passwordWrapper}>
                        <TextInput
                            style={styles.input}
                            placeholder="비밀번호를 입력해주세요"
                            placeholderTextColor="#BDBDBD"
                            value={password}
                            onChangeText={handlePasswordChange}
                            secureTextEntry={!showPassword}
                        />
                        <TouchableOpacity onPress={() => setShowPassword(!showPassword)}>
                            <Icon
                                name={showPassword ? "eye-off" : "eye"}
                                size={24}
                                color="#757575"
                            />
                        </TouchableOpacity>
                    </View>
                    {!isPasswordValid && password.length > 0 && (
                        <Text style={styles.errorText}>비밀번호는 영문, 숫자, 특수문자 포함 10자 이상이어야 합니다.</Text>
                    )}
                </View>

                {step === 2 && (
                    <View style={styles.inputContainer}>
                        <Text style={styles.label}>비밀번호 확인</Text>
                        <View style={styles.passwordWrapper}>
                            <TextInput
                                style={styles.input}
                                placeholder="비밀번호 재입력"
                                placeholderTextColor="#BDBDBD"
                                value={confirmPassword}
                                onChangeText={handleConfirmPasswordChange}
                                secureTextEntry={!showConfirmPassword}
                            />
                            <TouchableOpacity onPress={() => setShowConfirmPassword(!showConfirmPassword)}>
                                <Icon
                                    name={showConfirmPassword ? "eye-off" : "eye"}
                                    size={24}
                                    color="#757575"
                                />
                            </TouchableOpacity>
                        </View>
                        {!doPasswordsMatch && confirmPassword.length > 0 && (
                            <Text style={styles.errorText}>비밀번호가 일치하지 않습니다.</Text>
                        )}
                    </View>
                )}
            </ScrollView>

            <View style={styles.buttonContainer}>
                <TouchableOpacity
                    style={[styles.nextButton,
                        (step === 1 && !isPasswordValid) || (step === 2 && !doPasswordsMatch) ? styles.disabledButton : null]}
                    disabled={(step === 1 && !isPasswordValid) || (step === 2 && !doPasswordsMatch)}
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
    passwordWrapper: {
        flexDirection: "row",
        alignItems: "center",
        borderBottomWidth: 1,
        borderBottomColor: "#ddd",
    },
    input: {
        flex: 1,
        height: 50,
        fontSize: 16,
        paddingHorizontal: 10,
    },
    eyeIcon: {
        fontSize: 20,
        padding: 10,
    },
    errorText: {
        color: "red",
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
});