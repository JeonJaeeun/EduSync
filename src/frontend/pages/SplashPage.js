import React, { useEffect } from "react";
import { View, Image, StyleSheet } from "react-native";

export default function SplashScreen({ navigation }) {
    useEffect(() => {
        setTimeout(() => {
            if (navigation) {
                navigation.replace("Login"); // 네비게이션이 존재할 때만 실행
            }
        }, 2000);
    }, [navigation]);

    return (
        <View style={styles.container}>
            <Image source={require("../assets/splash-logo.png")} style={styles.logo} />
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "#007AFF",
    },
    logo: {
        width: 100,
        height: 100,
        resizeMode: "contain",
    },
});
