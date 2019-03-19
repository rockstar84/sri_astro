// @flow
import React from 'react';
import {Router, Route, Switch} from 'react-router-dom';
import {connect} from 'react-redux';

import type {Dispatch} from 'redux';
import type {Action} from '../types/Action';
import type {Alert} from '../types/Custom';

import {history} from '../_helpers';
import {alertActions} from '../_actions';
import {NavBar, SideBar, SidebarMenu, SidebarItem, Footer} from '../components';
import PerfectScrollbar from 'perfect-scrollbar';

import {Home} from '../components/Home';
import {NotFound} from '../components/NotFound';
import {Register} from '../components/Register';
import * as Waves from 'node-waves';
import 'perfect-scrollbar/css/perfect-scrollbar.css';
import './App.scss';

type Props = {
  dispatch: Dispatch<Action>,
  alert: Alert
};

class App extends React.Component<Props> {
  constructor(props: Props) {
    super(props);

    this.state = {
      gists: [
        {
          icon: 'fa fa-tachometer-alt',
          desc: 'Dashboards',
          children: [{link: '/', desc: 'Home'}, {link: '../dash/dash02.html', desc: 'Version 2'}]
        },
        {
          icon: 'fas fa-user-tie',
          desc: 'User',
          children: [
            {link: '../pages/login.html', desc: 'Login'},
            {link: '/register', icon: 'fa fa-bolt', desc: 'Register'}
          ]
        },
        {link: '/g/12345', icon: 'far fa-bell', desc: 'Alerts'}
      ]
    };

    const {dispatch} = this.props;
    history.listen(() => {
      // clear alert on location change
      dispatch(alertActions.clear());
    });
  }

  /* eslint-disable no-undef */
  componentDidMount() {
    new PerfectScrollbar('#root');
    Waves.attach('.button', ['waves-button', 'waves-float']);
    Waves.init();
  }

  render() {
    const {alert} = this.props;
    const {gists} = this.state;
    return (
      <Router history={history}>
        <React.Fragment>
          {alert.message && <div className={`alert ${alert.type}`}>{alert.message}</div>}
          <header>
            <SideBar>
              {gists ? (
                gists.map((gist: any) => {
                  return gist.children ? (
                    <SidebarMenu key={gist.desc} icon={gist.icon} desc={gist.desc}>
                      {gist.children.map((subGist: any) => {
                        return (
                          <SidebarItem key={subGist.link} icon={subGist.icon} link={subGist.link} desc={subGist.desc} />
                        );
                      })}
                    </SidebarMenu>
                  ) : (
                    <SidebarItem key={gist.link} link={gist.link} icon={gist.icon} desc={gist.desc} />
                  );
                })
              ) : (
                <div>Loading ... </div>
              )}
            </SideBar>
            <NavBar />
          </header>
          <main>
            <Switch>
              <Route exact path='/' component={Home} />
              <Route path='/register' component={Register} />
              <Route path='/g/:gistId' component={Gist} />
              <Route component={NotFound} />
            </Switch>
          </main>
          <Footer />
        </React.Fragment>
      </Router>
    );
  }
}

const Gist = (props: any) => {
  return <div>{props.match.params.gistId}</div>;
};

const mapStateToProps = (state: any) => {
  const {alert} = state;
  return {
    alert
  };
};

const connectedApp = connect(mapStateToProps)(App);
export {connectedApp as App};
