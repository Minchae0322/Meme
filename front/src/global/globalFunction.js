
import axios from "axios";
import { ref, onMounted, onUpdated} from "vue";


import { useRouter } from "vue-router";


const router = useRouter()
export const checkLogin = function () {
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
        try {
            axios.get("http://localhost:8080/auth/isValidToken", {
                headers: {
                    'Authorization': accessToken
                }
            }).then(response => {
                if (response.status === 200) {
                    console.log("실행 js 파일");
                    return true;
                } else {
                    return false;
                }
            });
        } catch (error) {
            console.error("에러 발생:", error);
            return false;
        }
    } else {
        return false;
    }
};