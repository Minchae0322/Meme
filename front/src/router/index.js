import { createRouter, createWebHistory } from 'vue-router'
import home from '../views/HomeView.vue'
import writePost from '../views/WriteView.vue';
import postList from '../views/PostListView.vue';
import login from "../views/login.vue";
import read from "../views/readView.vue";
import signup from "../views/SignupView.vue";
import update from "../views/UpdatePost.vue"
import userInfo from "../views/UserInfo.vue"
import app from "../App.vue";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/', // 도메인 입력 시 초기 페이지를 '/home'으로 설정
      redirect: '/home'
    },
    {
      path: '/home',
      name: 'home',
      component: home
    },
    {
      path: "/write",
      name: "write",
      component: writePost
    },
    {
      path: '/signup', // 회원가입 페이지 경로
      name: 'signup',
      component: signup, // 회원가입 컴포넌트
    },

    {
      path: "/posts",
      name: "postList",
      component: postList
    }

    ,
    {
      path: "/login",
      name: "login",
      component: login
    },

    {
      path: "/read/:postId",
      name: "read",
      component: read,
      props: true
    },

    {
      path: "/update/:postId",
      name: "update",
      component: update,
      props: true

    },

    {
      path: "/userInfo",
      name: "userInfo",
      component: userInfo
    }

    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AboutView.vue')
    // }
  ],

});

export default router
