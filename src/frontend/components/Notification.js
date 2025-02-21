// Notification.js
import React from "react";
import { View, Text, ScrollView, StyleSheet, Switch } from "react-native";
import Modal from "react-native-modal";

const Notification = ({ isModalVisible, toggleModal, isNotificationOn, setNotificationOn }) => {
    const notifications = [
        { type: "과외비 입금일 안내", message: "11월 27일은 김정우 학생의 과외비 지급일입니다!", color: "red", time: "12시간 전" },
        { type: "수업일지 작성", message: "아직 11월 06일 윤태원 학생의 수업일지를 작성하지 않으셔요!", color: "blue", time: "11월 13일" },
        { type: "수업일지 작성", message: "김정우 학생 9주차 수업일지에 코멘트가 담렸어요!", color: "blue", time: "11월 12일" },
        { type: "과외 구인구직", message: "새로운 과외 신청이 들어왔어요!", color: "green", time: "11월 11일" }
    ];

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