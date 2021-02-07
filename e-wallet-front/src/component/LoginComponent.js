import React from 'react'
import LoginService from '../service/LoginService'
import { Redirect } from "react-router-dom"

class LoginComponent extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            username: '',
            redirect: false
        }
        
        this.username = React.createRef()
        this.password = React.createRef()

        this.login = this.login.bind(this)
    }

    login() {
        LoginService.authenticateUser({
            'username': this.username.current.value,
            'password': this.password.current.value
        }).then((response) => {
            localStorage.setItem(response.data.username, response.data.token)
            this.setState({
                username: response.data.username,
                redirect: true
            })
        })
    }

    render() {
        if (this.state.redirect) {
            return <Redirect to={{ pathname: "/wallets", state: { username: this.username.current.value }}} />
        }

        return (
            <div align="center">
                <div className="w-25 p-3">
                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input type="text" ref={this.username} className="form-control" id="username" aria-describedby="emailHelp" placeholder="Username" />
                        <small id="usernameHelp" className="form-text text-muted">Strange hint: <b>admin</b></small>
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input type="password" ref={this.password} className="form-control" id="password" placeholder="Password" />
                        <small id="passwordHelp" className="form-text text-muted">Strange hint: <b>admin</b></small>
                    </div>
                    <button type="button" className="btn btn-primary" onClick={this.login}>Login</button>
                </div>
            </div>
        )
    }
}

export default LoginComponent