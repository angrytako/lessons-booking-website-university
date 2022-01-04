<template>
    <div class="home">
        <img alt="Vue logo" src="../assets/logo.png">
    </div>

    <ul>
        <li v-for="slot in $store.state.slots" :key="slot.course + slot.teacherName + slot.teacherSurname + slot.day + slot.time">
            <div class="slot">
                <div class="course">{{ slot.course }}</div>
                <div class="teacherName">{{ slot.teacherName }}</div>
                <div class="teacherSurname">{{ slot.teacherSurname }}</div>
                <div class="day">{{ slot.day }}</div>
                <div class="time">{{ slot.time }}</div>
            </div>

        </li>
    </ul>
</template>

<script>
    async function getSlots() {
        try {
            const response = await fetch("/Noodle_war/AvailableSlotsServlet");
            if (response.status == 401) {
                window.location.href = "/Noodle_war/login";
                return [];
            }
            return await response.json();
        } catch (e) {
            console.log(e);
        }
    }

    export default {
        name: 'Home',
        async created() {
            this.$store.state.slots = await getSlots();
        },
        data() {
        }

    }
</script>

<style scoped>
    .home{
        background-color: #f2e7c3;
    }
    li {
        text-decoration: none;
    }

    .slot {
        display: flex;
    }

</style>
