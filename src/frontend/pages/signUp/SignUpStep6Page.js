import React, { useState } from "react";
import {
    View,
    Text,
    TouchableOpacity,
    ScrollView,
    StyleSheet,
} from "react-native";
import { useRoute, useNavigation } from "@react-navigation/native";
import SignUpHeader from "../../components/SignUpHeader";
import Icon from "react-native-vector-icons/Feather"; // 체크 아이콘 추가

export default function SignUpStep6Page() {
    const route = useRoute();
    const navigation = useNavigation();

    const { email, password, nickname, phone, userTypes, schoolName } = route.params;

    const [termsAccepted, setTermsAccepted] = useState(false);
    const [personalInfoAccepted, setPersonalInfoAccepted] = useState(false);
    const [ageConfirmed, setAgeConfirmed] = useState(false);

    const allChecked = termsAccepted && personalInfoAccepted;

    const handleCompleteSignUp = () => {
        if (allChecked) {
            const userData = {
                email,
                password,
                nickname,
                phone,
                userTypes,
                ...(userTypes === "학생" && { schoolName }) // 학생일 때만 schoolName 포함
            };
            console.log("회원가입 정보:", userData);
            // 이후 회원가입 처리 로직 추가

            navigation.navigate("Login");
        }
    };

    return (
        <View style={styles.wrapper}>
            <ScrollView contentContainerStyle={styles.container}>
                <SignUpHeader
                    title="내 손안에 과외수업!"
                    subtitle="앱을 사용하려면 동의해주세요!"
                />

                {/* 모두 체크하기 버튼 */}
                <TouchableOpacity
                    style={[styles.allCheckButton, allChecked && styles.checkedButton]}
                    onPress={() => {
                        const newState = !allChecked;
                        setTermsAccepted(newState);
                        setPersonalInfoAccepted(newState);
                        setAgeConfirmed(newState);
                    }}
                >
                    <Icon name="check" size={20} color="white" />
                    <Text style={styles.allCheckText}>모두 체크하기</Text>
                </TouchableOpacity>

                {/* 약관 동의 영역 */}
                <View style={styles.agreementContainer}>
                    {[
                        {
                            text: "[필수] 이용약관 필수동의",
                            state: termsAccepted,
                            setState: setTermsAccepted,
                        },
                        {
                            text: "[필수] 개인정보 취급방침 필수동의",
                            state: personalInfoAccepted,
                            setState: setPersonalInfoAccepted,
                        },
                        {
                            text: "[선택] 만 14세 이상입니다",
                            state: ageConfirmed,
                            setState: setAgeConfirmed,
                        },
                    ].map((item, index) => (
                        <TouchableOpacity
                            key={index}
                            style={styles.agreementRow}
                            onPress={() => item.setState(!item.state)}
                        >
                            <Icon
                                name="check"
                                size={20}
                                color={item.state ? "#007AFF" : "#ccc"}
                                style={styles.checkIcon}
                            />
                            <Text style={styles.checkText}>{item.text}</Text>
                        </TouchableOpacity>
                    ))}
                </View>
            </ScrollView>

            <View style={styles.buttonContainer}>
                <TouchableOpacity
                    style={[styles.nextButton, !allChecked && styles.disabledButton]}
                    disabled={!allChecked}
                    onPress={handleCompleteSignUp}
                >
                    <Text style={styles.nextButtonText}>회원가입 완료</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    wrapper: {
        flex: 1,
        backgroundColor: "#FFF",
    },
    container: {
        paddingHorizontal: 20,
        paddingTop: 80,
        paddingBottom: 100, // 버튼과 겹치지 않도록 여백 추가
    },
    allCheckButton: {
        flexDirection: "row",
        alignItems: "center",
        justifyContent: "center",
        backgroundColor: "#ccc",
        paddingVertical: 15,
        borderRadius: 10,
        marginBottom: 20,
    },
    checkedButton: {
        backgroundColor: "#007AFF",
    },
    allCheckText: {
        color: "#FFF",
        fontSize: 16,
        fontWeight: "bold",
        marginLeft: 10,
    },
    agreementContainer: {
        marginVertical: 20,
    },
    agreementRow: {
        flexDirection: "row",
        alignItems: "center",
        paddingVertical: 12,
    },
    checkIcon: {
        marginRight: 10,
    },
    checkText: {
        fontSize: 16,
        color: "#333",
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