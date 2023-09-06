import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import WriteView from '../views/WriteView.vue';
import PostListView from '../views/PostListView.vue';
import login from "../views/login.vue";
import read from "../views/readView.vue";
import Signup from "../views/SignupView.vue";
import app from "../App.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/home',
      name: 'home',
      component: HomeView
    },
    {
      path: "/write",
      name: "write",
      component: WriteView
    },
    {
      path: '/signup', // 회원가입 페이지 경로
      name: 'Signup',
      component: Signup, // 회원가입 컴포넌트
    },

    {
      path: "/posts",
      name: "postList",
      component: PostListView
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
