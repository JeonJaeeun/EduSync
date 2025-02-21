import React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import SplashScreen from "./pages/SplashPage";
import LoginScreen from "./pages/LoginPage";
import SignUpStep1Screen from "./pages/signUp/SignUpStep1Page";
import SignUpStep2Screen from "./pages/signUp/SignUpStep2Page";
import SignUpStep3Screen from "./pages/signUp/SignUpStep3Page";
import SignUpStep4Screen from "./pages/signUp/SignUpStep4Page";
import SignUpStep5Screen from "./pages/signUp/SignUpStep5Page";
import SignUpStep6Screen from "./pages/signUp/SignUpStep6Page";
import HomeScreen from "./pages/HomePage";
import Header from "./components/Header";
//import StudentManagementScreen from "./pages/StudentManagementPage";
//import StudentGradeScreen from "./pages/StudentGradePage";
import StudentManagementStack from "./stacks/StudentManagementStack";

const Stack = createStackNavigator();
export default function App() {

    return (
        <NavigationContainer>
            <Stack.Navigator screenOptions={{ headerShown: false }}>
                <Stack.Screen name="Splash" component={SplashScreen} />
                <Stack.Screen name="Login" component={LoginScreen} />
                <Stack.Screen name="SignupStep1" component={SignUpStep1Screen} />
                <Stack.Screen name="SignupStep2" component={SignUpStep2Screen} />
                <Stack.Screen name="SignupStep3" component={SignUpStep3Screen} />
                <Stack.Screen name="SignupStep4" component={SignUpStep4Screen} />
                <Stack.Screen name="SignupStep5" component={SignUpStep5Screen} />
                <Stack.Screen name="SignupStep6" component={SignUpStep6Screen} />
                <Stack.Screen name="Home" component={HomeScreen} />
                <Stack.Screen name="StudentManagement" component={StudentManagementStack} />
            </Stack.Navigator>
        </NavigationContainer>
    );
}
