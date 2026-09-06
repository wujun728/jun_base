import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const files = require.context("./modules", false, /\.js$/);
let modules = {
	state: {},
	mutations: {},
	actions: {},
  getters: {}
};

files.keys().forEach((key) => {
  Object.assign(modules.state, files(key)["state"] || {});
  Object.assign(modules.mutations, files(key)["mutations"] || {});
  Object.assign(modules.actions, files(key)["actions"] || {});
  Object.assign(modules.getters, files(key)["getters"] || {});
});
const store = new Vuex.Store(modules);
export default store;
