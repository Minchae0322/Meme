import { createStore } from 'vuex';

export default createStore({
    state: {
        isLogin: false,
    },
    mutations: {
        setIsLogin(state, newValue) {
            state.isLogin = newValue;
        },
    },
    actions: {
        setLoginStatus({ commit }, newValue) {
            commit('setIsLogin', newValue);
        },
    },
    getters: {
        isUserLoggedIn: (state) => state.isLogin === '로그아웃',
    },
});