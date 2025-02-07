import React from "react";
import { View, Text, StyleSheet } from "react-native";

export default function SignUpHeader({ title, subtitle }) {
    return (
        <View style={styles.headerContainer}>
            <Text style={styles.title}>{title}</Text>
            {subtitle ? <Text style={styles.subtitle}>{subtitle}</Text> : null}
        </View>
    );
}

const styles = StyleSheet.create({
    headerContainer: {
        alignItems: "flex-start",
        paddingTop: 70,
        marginBottom: 50,
    },
    title: {
        fontSize: 28,
        fontWeight: "bold",
        marginBottom: 10,
    },
    subtitle: {
        fontSize: 14,
        color: "#555",
    },
});
