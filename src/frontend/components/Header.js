// Header.js
import React, { useState } from "react";
import { View, Image, Text, TouchableOpacity, StyleSheet } from "react-native";
import Ionicons from "react-native-vector-icons/Ionicons";
import { useNavigation, useNavigationState } from "@react-navigation/native";
import Notification from "./Notification";

const Header = () => {
    const navigation = useNavigation();
    const currentRoute = useNavigationState(state => state?.routes[state.index]?.name);

    const [isNotificationOn, setNotificationOn] = useState(true);
    const [isModalVisible, setModalVisible] = useState(false);

    const toggleModal = () => {
        setModalVisible(!isModalVisible);
    };

    return (
        <View style={styles.container}>
            <View style={styles.headerRow}>
                <Image source={require("../assets/logo.png")} style={styles.logo} resizeMode="contain" />
                <TouchableOpacity style={styles.notificationButton} onPress={toggleModal}>
                    <Ionicons name={isNotificationOn ? "notifications-outline" : "notifications-off-outline"} size={28} color="#000" />
                </TouchableOpacity>
            </View>

            <View style={styles.navBar}>
                {["Home", "ClassManagement", "StudentManagement", "MyPage"].map((routeName, index) => (
                    <TouchableOpacity key={index} onPress={() => navigation.navigate(routeName)}>
                        <Text style={currentRoute === routeName ? styles.active : styles.inactive}>
                            {routeName === "Home" ? "홈" :
                                routeName === "ClassManagement" ? "수업관리" :
                                    routeName === "StudentManagement" ? "학생관리" : "마이페이지"}
                        </Text>
                    </TouchableOpacity>
                ))}
            </View>

            <Notification
                isModalVisible={isModalVisible}
                toggleModal={toggleModal}
                isNotificationOn={isNotificationOn}
                setNotificationOn={setNotificationOn}
            />
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        backgroundColor: "#fff",
        borderBottomWidth: 1,
        borderColor: "#ddd",
        paddingBottom: 16,
        paddingHorizontal: 16,
    },
    headerRow: {
        flexDirection: "row",
        alignItems: "center",
        justifyContent: "space-between",
    },
    logo: {
        width: 120,
        height: 32,
    },
    notificationButton: {
        padding: 8,
    },
    navBar: {
        flexDirection: "row",
        justifyContent: "space-evenly",
        marginTop: 16,
    },
    active: {
        fontSize: 16,
        fontWeight: "bold",
        color: "#000",
    },
    inactive: {
        fontSize: 16,
        color: "#999",
    },
});

export default Header;