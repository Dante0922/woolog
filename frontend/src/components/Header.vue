<template>
  <header>

    <el-header class="header">
      <el-menu
          mode="horizontal"
      >
        <el-menu-item index="1">
          <router-link to="/">Home</router-link>
        </el-menu-item>
        <el-menu-item index="2">
          <router-link to="/write">글 작성</router-link>
        </el-menu-item>

      </el-menu>
    </el-header>

  </header>
</template>

<script setup lang="ts"></script>

<style scoped>
.header {
  padding: 0;
  height: 60px;
}
</style>