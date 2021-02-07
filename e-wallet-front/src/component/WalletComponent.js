import React from 'react'
import WalletService from '../service/WalletService'
import { Redirect } from "react-router-dom"
import axios from 'axios'

class WalletComponent extends React.Component {
    constructor(props) {
        super(props)
        
        const token = localStorage.getItem(this.props.location.state.username)
        
        // Setting default bearer for all requests
        axios.defaults.headers.common = { 'Authorization': 'Bearer ' + token }

        this.state = {
            wallets: [],
            redirect: false
        }
        
        this.createRefs()
        this.bindHandlers()
    }

    /**
     * Creating refs for proper interraction with input fields
     */
    createRefs() {
        this.changeAmount = React.createRef()
        this.changeWalletId = React.createRef()

        this.fromWalletId = React.createRef()
        this.toWalletId = React.createRef()
        this.transferAmount = React.createRef()

        this.newName = React.createRef()
        this.newBalance = React.createRef()

        this.deleteWalletId = React.createRef()
    }

    /**
     * Binding handlers for proper this context
     */
    bindHandlers() {
        this.withdrawFromWallet = this.withdrawFromWallet.bind(this)
        this.deleteSelected = this.deleteSelected.bind(this)
        this.addToWallet = this.addToWallet.bind(this)
        this.transferToWallet = this.transferToWallet.bind(this)
        this.createWallet = this.createWallet.bind(this)
    }

    componentDidMount() {
        this.getAllWallets()
    }

    getAllWallets() {
        WalletService.getAllWallets(this.state.token).then((response) => {
            this.setState({wallets: response.data})
        }).catch((error) => {
            this.alertErrorOrRedirect(error)
        })
    }

    /**
     * Again we're making copy instead of direct state manipulation
     */
    createWallet() {
        WalletService.createWallet({
            'name': this.newName.current.value,
            'balance': this.newBalance.current.value
        }).then((response) => {
            let stateCopy = [...this.state.wallets]
            stateCopy.push(response.data)
            this.setState({
                wallets: stateCopy
            })    
        }).catch((error) => {
            this.alertErrorOrRedirect(error)
        })
    }

    deleteSelected() {
        let forDeleting = this.deleteWalletId.current.value
        WalletService.deleteById(forDeleting).then((response) => {
            let stateCopy = [...this.state.wallets]
            const elemIndex = this.state.wallets.findIndex(elem => elem.id == forDeleting)
            stateCopy.splice(elemIndex, 1)
            this.setState({
                wallets: stateCopy
            })
        }).catch((error) => {
            this.alertErrorOrRedirect(error)
        })
    }

    transferToWallet() {
        WalletService.transferToWallet({
            'fromWalletId': this.fromWalletId.current.value,
            'toWalletId': this.toWalletId.current.value,
            'amount': this.transferAmount.current.value
        }).then((response) => {
            this.updateBalance(response.data)
        }).catch((error) => {
            this.alertErrorOrRedirect(error)
        })
    }

    withdrawFromWallet() {
        WalletService.withdrawFromWallet(this.changeWalletId.current.value, 
            {'amount': this.changeAmount.current.value}).then((response) => {
                this.updateBalance([response.data])
        }).catch((error) => {
            this.alertErrorOrRedirect(error)
        })
    }

    addToWallet() {
        WalletService.addToWallet(this.changeWalletId.current.value, 
            {'amount': this.changeAmount.current.value}).then((response) => {
                this.updateBalance([response.data])    
        }).catch((error) => {
            this.alertErrorOrRedirect(error)
        })
    }

    /**
     * Instead of direct state manipulation we're making copy and replacing with state
     * @param {*} data with updated balance 
     */
    updateBalance(data) {
        let stateCopy = [...this.state.wallets]
        data.map((wallet) => {
            const elemIndex = this.state.wallets.findIndex(elem => elem.id == wallet.id)
            stateCopy[elemIndex] = {...stateCopy[elemIndex], balance: wallet.balance}
        })
        this.setState({
            wallets: stateCopy,
        })
    }

    /**
     * Simple alerting of errors. Welcome to UI/UX hell
     */
    alertErrorOrRedirect(error) {
        if (error.response) {
            if (error.response.status === 401 || error.response.status === 403) {
                // Remove token because of expired
                localStorage.removeItem(this.props.location.state.username)
                this.setState({
                    redirect: true
                })
                return
            }
            alert(error.response.data.message)
        }
    }

    render() {
        if (this.state.redirect) {
            return <Redirect to="/login" />
        }

        return (
            <div>
                <h1 className="text-center">Active Wallets</h1>
                <table className="table table-striped">
                    <thead>
                        <tr>
                            <td>ID</td>
                            <td>Name</td>
                            <td>Balance</td>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            this.state.wallets.map((wallet) =>
                                <tr key={wallet.id}>
                                    <td>{wallet.id}</td>
                                    <td>{wallet.name}</td>
                                    <td>{wallet.balance}</td>
                                </tr>
                            )
                        }
                    </tbody>
                </table>
                <div className="input-group w-75 p-3">
                    <div className="input-group-prepend">
                        <button className="btn btn-outline-secondary" type="button" onClick={this.createWallet}>Create</button>
                    </div>
                    <input ref={this.newName} type="text" className="form-control" placeholder="Wallet Name" aria-label="" aria-describedby="basic-addon1"></input>
                    <input ref={this.newBalance} type="number" className="form-control" placeholder="Balance" aria-label="" aria-describedby="basic-addon1"></input>
                </div>
                <div className="input-group w-25 p-3">
                    <div className="input-group-prepend">
                        <button className="btn btn-outline-secondary" type="button" onClick={this.deleteSelected}>Delete</button>
                    </div>
                    <input ref={this.deleteWalletId} type="number" className="form-control" placeholder="Wallet ID" aria-label="" aria-describedby="basic-addon1"></input>
                </div>
                <div className="input-group w-50 p-3">
                    <div className="input-group-prepend">
                        <button className="btn btn-outline-secondary" onClick={this.withdrawFromWallet} type="button">Withdraw</button>
                        <button className="btn btn-outline-secondary" onClick={this.addToWallet} type="button">Add</button>
                    </div>
                    <input ref={this.changeWalletId} type="number" className="form-control" placeholder="Wallet ID" aria-label="" aria-describedby="basic-addon1"></input>        
                    <input ref={this.changeAmount} type="number" className="form-control" placeholder="Amount" aria-label="" aria-describedby="basic-addon1"></input>
                </div>
                <div className="input-group w-50 p-3">
                    <div className="input-group-prepend">
                        <button className="btn btn-outline-secondary" type="button" onClick={this.transferToWallet}>Transfer</button>
                    </div>
                    <input ref={this.fromWalletId} type="number" className="form-control" placeholder="From Wallet ID" aria-label="" aria-describedby="basic-addon1"></input>
                    <input ref={this.toWalletId} type="number" className="form-control" placeholder="To Wallet ID" aria-label="" aria-describedby="basic-addon1"></input>
                    <input ref={this.transferAmount} type="number" className="form-control" placeholder="Amount" aria-label="" aria-describedby="basic-addon1"></input>
                </div>
                
            </div>
        )
    }
}

export default WalletComponent