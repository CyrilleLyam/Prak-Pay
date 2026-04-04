<template>
  <div class="min-h-screen bg-background">
    <div class="hidden h-screen lg:flex">
      <aside class="w-64 shrink-0 border-r bg-card">
        <SidebarNav />
      </aside>

      <main class="flex-1 p-6">
        <slot />
      </main>
    </div>

    <div class="flex min-h-screen items-center justify-center p-6 lg:hidden">
      <p class="text-center text-muted-foreground">
        Mobile view is not supported. Please use a desktop browser.
      </p>
    </div>
  </div>
</template>
