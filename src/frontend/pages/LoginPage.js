import React, { useState } from "react";
import {
    View,
    Text,
    TextInput,
    TouchableOpacity,
    StyleSheet,
    Image,
} from "react-native";
import Icon from "react-native-vector-icons/MaterialCommunityIcons";
import { useNavigation } from "@react-navigation/native";

export default function LoginScreen() {

    const navigation = useNavigation();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);

    // 로그인 버튼 활성화 여부
    const isLoginEnabled = email.length > 0 && password.length > 0;

    const onLoginPress = () => {
        // 로그인 로직 추가
        console.log("로그인 성공");
        navigation.navigate("Home");
    };

    return (
        <View style={styles.container}>
            {/* 로고 */}
            <Image source={require("../assets/login-logo.png")} style={styles.logo} />
            <Text style={styles.subtitle}>
                당신이 수업에 집중할 수 있도록 하는 학생 관리 서비스, 에듀싱크
            </Text>

            {/* 이메일 입력 */}
            <Text style={styles.label}>이메일 주소</Text>
            <TextInput
                style={styles.input}
                placeholder="예) Edu_Sync@edusync.com"
                placeholderTextColor="#BDBDBD"
                value={email}
                onChangeText={setEmail}
                keyboardType="email-address"
            />

            {/* 비밀번호 입력 */}
            <Text style={styles.label}>비밀번호</Text>
            <View style={styles.passwordContainer}>
                <TextInput
                    style={styles.passwordInput}
                    placeholder="비밀번호 입력"
                    placeholderTextColor="#BDBDBD"
                    secureTextEntry={!showPassword}
                    value={password}
                    onChangeText={setPassword}
                />
                <TouchableOpacity onPress={() => setShowPassword(!showPassword)}>
                    <Icon
                        name={showPassword ? "eye-off" : "eye"}
                        size={24}
                        color="#757575"
                    />
                </TouchableOpacity>
            </View>

            {/* 로그인 버튼 */}
            <TouchableOpacity
                style={[styles.loginButton, isLoginEnabled ? styles.loginActive : null]}
                disabled={!isLoginEnabled}
                onPress={onLoginPress}
            >
                <Text style={styles.loginText}>로그인</Text>
            </TouchableOpacity>

            {/* 회원가입 / 비밀번호 찾기 */}
            <View style={styles.bottomContainer}>
                <TouchableOpacity onPress={()=>navigation.navigate("SignupStep1")}>
                    <Text style={styles.signup}>회원가입</Text>
                </TouchableOpacity>
                <Text style={styles.divider}>|</Text>
                <TouchableOpacity>
                    <Text style={styles.forgotPassword}>비밀번호 찾기</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#FFFFFF",
        alignItems: "center",
        paddingTop: 160,
    },
    logo: {
        width: 150,
        height: 60,
        resizeMode: "contain",
        marginBottom: 10,
    },
    subtitle: {
        fontSize: 14,
        color: "#555",
        marginBottom: 50,
        textAlign: "center",
    },
    label: {
        alignSelf: "flex-start",
        fontSize: 16,
        fontWeight: "bold",
        marginBottom: 5,
        marginLeft: 20,
    },
    input: {
        width: "90%",
        height: 50,
        borderBottomWidth: 1,
        borderBottomColor: "#BDBDBD",
        fontSize: 16,
        marginBottom: 20,
        paddingHorizontal: 10,
    },
    passwordContainer: {
        flexDirection: "row",
        alignItems: "center",
        width: "90%",
        height: 50,
        borderBottomWidth: 1,
        borderBottomColor: "#BDBDBD",
        marginBottom: 20,
    },
    passwordInput: {
        flex: 1,
        fontSize: 16,
        paddingHorizontal: 10,
    },
    loginButton: {
        width: "90%",
        height: 50,
        backgroundColor: "#E0E0E0",
        borderRadius: 10,
        alignItems: "center",
        justifyContent: "center",
        marginTop: 20,
    },
    loginActive: {
        backgroundColor: "#007AFF",
    },
    loginText: {
        fontSize: 18,
        color: "#FFFFFF",
        fontWeight: "bold",
    },
    bottomContainer: {
        flexDirection: "row",
        marginTop: 20,
    },
    signup: {
        fontSize: 14,
        color: "#007AFF",
    },
    forgotPassword: {
        fontSize: 14,
        color: "#757575",
    },
    divider: {
        marginHorizontal: 10,
        color: "#757575",
    },
});
