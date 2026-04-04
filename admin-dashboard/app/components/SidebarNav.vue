<script setup lang="ts">
import {
  LayoutDashboard,
  CreditCard,
  Bell,
  Radio,
} from "lucide-vue-next";

const emit = defineEmits<{ navigate: [] }>();

const navItems = [
  { label: "Dashboard", icon: LayoutDashboard, to: "/" },
  { label: "Transaction", icon: CreditCard, to: "/transaction" },
  { label: "Notification", icon: Bell, to: "/notification" },
  { label: "Real Time Notification", icon: Radio, to: "/real-time-notification" },
];
</script>

<template>
  <div class="flex h-16 items-center gap-2 border-b px-6">
    <CreditCard class="size-6 text-primary" />
    <span class="text-lg font-semibold">Prak Pay</span>
  </div>

  <nav class="flex flex-col gap-1 p-4">
    <NuxtLink
      v-for="item in navItems"
      :key="item.to"
      :to="item.to"
      class="flex items-center gap-3 rounded-md px-3 py-2 text-sm font-medium text-muted-foreground transition-colors hover:bg-accent hover:text-accent-foreground [&.router-link-exact-active]:bg-accent [&.router-link-exact-active]:text-accent-foreground"
      @click="emit('navigate')"
    >
      <component :is="item.icon" class="size-4" />
      {{ item.label }}
    </NuxtLink>
  </nav>
</template>
