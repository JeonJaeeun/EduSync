// Notification.js
import React, { useEffect, useState } from "react";
import { View, Text, ScrollView, StyleSheet, Switch } from "react-native";
import Modal from "react-native-modal";
import { getNotifications } from "../utils/NotificationUtils";

const Notification = ({ isModalVisible, toggleModal, isNotificationOn, setNotificationOn }) => {

    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        // 알림 목록 불러오기
        async function fetchNotifications() {
            const fetchedNotifications = await getNotifications();
            setNotifications(fetchedNotifications);
        }

        fetchNotifications();
    }, []);

    return (
        <Modal isVisible={isModalVisible} onBackdropPress={toggleModal} backdropTransitionOutTiming={0} style={styles.modalContainer}>
            <View style={styles.modalContent}>
                <View style={styles.modalHandle} />
                <View style={styles.modalHeader}>
                    <Text style={styles.modalTitle}>알림</Text>
                    <Switch style={styles.smallSwitch} value={isNotificationOn} onValueChange={setNotificationOn} />
                </View>
                <ScrollView style={styles.notificationList}>
                    {notifications.length > 0 ? (
                        notifications.map((noti, index) => (
                            <View key={index} style={styles.notificationItem}>
                                <View style={styles.notificationTop}>
                                    <View style={[styles.notificationCircle, { backgroundColor: noti.color }]} />
                                    <Text style={styles.notificationType}>{noti.type}</Text>
                                    <Text style={styles.notificationTime}>{noti.time}</Text>
                                </View>
                                <Text style={styles.notificationMessage}>{noti.message}</Text>
                            </View>
                        ))
                    ) : (
                        <Text style={styles.notificationText}>새로운 알림이 없습니다.</Text>
                    )}
                </ScrollView>
            </View>
        </Modal>
    );
};

const styles = StyleSheet.create({
    modalContainer: {
        justifyContent: "flex-end",
        margin: 0,
    },
    modalContent: {
        backgroundColor: "#fff",
        padding: 20,
        borderTopLeftRadius: 20,
        borderTopRightRadius: 20,
    },
    modalHandle: {
        width: 40,
        height: 4,
        backgroundColor: "#ccc",
        borderRadius: 2,
        marginBottom: 10,
        marginLeft: 150,
    },
    modalHeader: {
        flexDirection: "row",
        alignItems: "center",
        marginBottom: 10,
    },
    modalTitle: {
        fontSize: 18,
        fontWeight: "bold",
        marginLeft: 10,
    },
    smallSwitch: {
        transform: [{ scaleX: 0.7 }, { scaleY: 0.7 }],
        marginRight: 10,
    },
    notificationList: {
        width: "100%",
        maxHeight: 300,
    },
    notificationItem: {
        padding: 10,
        marginVertical: 5,
        borderRadius: 8,
    },
    notificationTop: {
        flexDirection: "row",
        alignItems: "center",
        justifyContent: "space-between",
        marginBottom: 7,
    },
    notificationCircle: {
        width: 10,
        height: 10,
        borderRadius: 5,
        marginRight: 8,
    },
    notificationType: {
        fontSize: 12,
        color: "#777",
        flex: 1,
    },
    notificationTime: {
        fontSize: 12,
        color: "#999",
    },
    notificationMessage: {
        fontSize: 16,
        marginLeft: 17,
    },
    notificationText: {
        textAlign: "center",
        color: "#666",
        marginTop: 10,
    },
});

export default Notification;