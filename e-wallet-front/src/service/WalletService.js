import axios from 'axios'

const WALLET_URL_V1 = "http://localhost:9090/v1/wallets"

class WalletService {
    getAllWallets() {
        return axios.get(WALLET_URL_V1)
    }

    createWallet(data) {
        return axios.post(`${WALLET_URL_V1}`, data)
    }

    withdrawFromWallet(walletId, data) {
        return axios.patch(`${WALLET_URL_V1}/${walletId}/withdraw`, data)
    }

    deleteById(walletId) {
        return axios.delete(`${WALLET_URL_V1}/${walletId}`)
    }

    addToWallet(walletId, data) {
        return axios.patch(`${WALLET_URL_V1}/${walletId}/add`, data)
    }

    transferToWallet(data) {
        return axios.patch(`${WALLET_URL_V1}/transfer`, data)
    }
}

export default new WalletService()