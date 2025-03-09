import * as Notifications from 'expo-notifications';
import { Platform } from 'react-native';
import Constants from 'expo-constants';

async function registerForPushNotificationsAsync() {
    let token;

    if (Platform.OS === 'ios') {
        console.log('📌 iOS 푸시 토큰 요청 시작');
        try {
            const { status } = await Notifications.requestPermissionsAsync();
            console.log('🔍 iOS 알림 권한 상태:', status);

            if (status !== 'granted') {
                console.log('❌ iOS 푸시 알림 권한 거부됨');
                return null;
            }

            token = (await Notifications.getExpoPushTokenAsync({
                projectId: Constants.expoConfig.extra.eas.projectId,
            })).data;

            console.log('✅ iOS 푸시 토큰:', token);
        } catch (error) {
            console.error('🔥 Expo Push Token 가져오기 실패:', error);
            return null;
        }
    }

    return token;
}

// 백엔드로 푸시 토큰 전송
async function sendPushTokenToBackend(token) {
    try {
        const response = await fetch('http://172.30.1.18:8080/api/save-token', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ token, platform: Platform.OS }),
        });

        if (!response.ok) {
            throw new Error('푸시 토큰 저장 실패');
        }
        console.log('✅ 푸시 토큰 백엔드 저장 성공');
    } catch (error) {
        console.error('❌ 푸시 토큰 저장 실패:', error);
    }
}

// 푸시 알림 데이터 가져오기 (테스트용)
async function getNotifications() {
    return [
        { type: "과외비 입금일 안내", message: "11월 27일은 김정우 학생의 과외비 지급일입니다!", color: "red", time: "12시간 전" },
        { type: "수업일지 작성", message: "아직 11월 06일 윤태원 학생의 수업일지를 작성하지 않으셔요!", color: "blue", time: "11월 13일" },
        { type: "수업일지 작성", message: "김정우 학생 9주차 수업일지에 코멘트가 담겼어요!", color: "blue", time: "11월 12일" },
        { type: "과외 구인구직", message: "새로운 과외 신청이 들어왔어요!", color: "green", time: "11월 11일" }
    ];
}

export {getNotifications};
//export { registerForPushNotificationsAsync, getNotifications };