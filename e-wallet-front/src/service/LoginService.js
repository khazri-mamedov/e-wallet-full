import axios from 'axios'

const WALLET_URL_V1 = "http://localhost:9090/v1/users"

class LoginService {
    authenticateUser(data) {
        return axios.post(`${WALLET_URL_V1}/authenticate`, data)
    }
}

export default new LoginService() 