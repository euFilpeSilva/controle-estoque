import { createApp } from 'vue';
import App from './App.vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css'; // Import Element Plus CSS
import router from './router';

const app = createApp(App);

// Register Element Plus globally
app.use(ElementPlus);

app.use(router);
app.mount('#app');