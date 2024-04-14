export default {
  methods: {
    getUserName() {
      return localStorage.getItem('ms_username');
    },
    getIsAdmin() {
      return localStorage.getItem('isAdmin') === '管理员'
    },
    getUserId() {
      return localStorage.getItem('ms_userId');
    }
  }
}