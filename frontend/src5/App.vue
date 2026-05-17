<template>
  <div id="app">
    <Layout v-if="showLayout" />
    <router-view v-else />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Layout from './components/Layout/MainLayout.vue'

const route = useRoute()

const showLayout = computed(() => {
  const noLayoutPaths = ['/login', '/register']
  return !noLayoutPaths.includes(route.path)
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
}

#app {
  height: 100%;
}
</style>
