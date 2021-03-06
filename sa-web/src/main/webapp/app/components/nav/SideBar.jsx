// @flow
import { userActions } from "Actions";
import * as React from "react";
import { connect } from "react-redux";
import type { Dispatch } from "../../types";
import "./SideBar.scss";

type Props = {
  dispatch: Dispatch,
  children?: React.Node,
};

class SideBar extends React.Component<Props> {
  handleLogoutUser = () => this.props.dispatch(userActions.logout());

  openHandler = () => {};

  render() {
    return (
      <div id="slide-out" className="side-nav sn-bg-4 fixed">
        <ul>
          <li>
            <ul className="collapsible collapsible-accordion">{this.props.children}</ul>
          </li>
        </ul>
      </div>
    );
  }
}

export default connect()(SideBar);
