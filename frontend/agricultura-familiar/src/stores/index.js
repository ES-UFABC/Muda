import Vue from 'vue';
import Vuex from 'vuex';

import { layout }from './layout.module';
import { auth } from './auth.module';
import { search } from './search.module';
import { geolocation } from './geolocation.module';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    auth,
    layout,
    search,
    geolocation
  },
});
