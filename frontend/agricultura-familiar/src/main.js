import Vue from 'vue'
import './utils/app-init';
//import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
//import VueMask from 'v-mask';

//import './styles/app.scss';

//// Make BootstrapVue available throughout your project
//Vue.use(BootstrapVue)
//// Optionally install the BootstrapVue icon components plugin
//Vue.use(IconsPlugin)

//Vue.use(VueMask);


import App from './App'
import router from './routes';
import store from './stores';


new Vue({
  el: '#app',
  store,
  router,
  render: h => h(App)
})
