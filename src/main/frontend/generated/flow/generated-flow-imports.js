import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/details/theme/lumo/vaadin-details.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/horizontal-layout/theme/lumo/vaadin-horizontal-layout.js';
import '@vaadin/login/theme/lumo/vaadin-login-overlay.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import '@vaadin/combo-box/theme/lumo/vaadin-combo-box.js';
import 'Frontend/generated/jar-resources/comboBoxConnector.js';
import 'Frontend/generated/jar-resources/vaadin-grid-flow-selection-column.js';
import '@vaadin/grid/theme/lumo/vaadin-grid-column.js';
import '@vaadin/list-box/theme/lumo/vaadin-list-box.js';
import '@vaadin/app-layout/theme/lumo/vaadin-app-layout.js';
import '@vaadin/item/theme/lumo/vaadin-item.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import 'Frontend/generated/jar-resources/buttonFunctions.js';
import '@vaadin/checkbox-group/theme/lumo/vaadin-checkbox-group.js';
import 'Frontend/generated/jar-resources/menubarConnector.js';
import '@vaadin/menu-bar/theme/lumo/vaadin-menu-bar.js';
import '@vaadin/form-layout/theme/lumo/vaadin-form-layout.js';
import '@vaadin/dialog/theme/lumo/vaadin-dialog.js';
import '@vaadin/grid/theme/lumo/vaadin-grid-column-group.js';
import '@vaadin/confirm-dialog/theme/lumo/vaadin-confirm-dialog.js';
import '@vaadin/email-field/theme/lumo/vaadin-email-field.js';
import '@vaadin/icon/theme/lumo/vaadin-icon.js';
import '@vaadin/context-menu/theme/lumo/vaadin-context-menu.js';
import 'Frontend/generated/jar-resources/contextMenuConnector.js';
import 'Frontend/generated/jar-resources/contextMenuTargetConnector.js';
import '@vaadin/form-layout/theme/lumo/vaadin-form-item.js';
import '@vaadin/multi-select-combo-box/theme/lumo/vaadin-multi-select-combo-box.js';
import '@vaadin/grid/theme/lumo/vaadin-grid.js';
import '@vaadin/grid/theme/lumo/vaadin-grid-sorter.js';
import '@vaadin/checkbox/theme/lumo/vaadin-checkbox.js';
import 'Frontend/generated/jar-resources/gridConnector.ts';
import '@vaadin/text-field/theme/lumo/vaadin-text-field.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/date-picker/theme/lumo/vaadin-date-picker.js';
import 'Frontend/generated/jar-resources/datepickerConnector.js';
import '@vaadin/avatar/theme/lumo/vaadin-avatar.js';
import '@vaadin/select/theme/lumo/vaadin-select.js';
import 'Frontend/generated/jar-resources/selectConnector.js';
import 'Frontend/generated/jar-resources/lit-renderer.ts';
import '@vaadin/notification/theme/lumo/vaadin-notification.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '6bf6684e4fcff1c2d0c23db76d945fcf0925d0d9bdfbbdd7ee5af9381857377c') {
    pending.push(import('./chunks/chunk-2f99813261917c6c6428af76f0781150415c7a99cc21997bd51b40c695a2e5ca.js'));
  }
  if (key === '591b35726c0965c47800b8530302006e4659d85ff957c4d9c30d790b21bad0cf') {
    pending.push(import('./chunks/chunk-a299c2956a51fb8b171f932cee178a64a1b5f6ec9080728c009853465cd1f6e6.js'));
  }
  if (key === '1fd3aea0c0c3575e97c123025827d179957274339a489f7738175688ee55d877') {
    pending.push(import('./chunks/chunk-876861b2ee5eda5003b496127fbf5061cc253f83b212c4b11f5b81de9b2004a8.js'));
  }
  if (key === '2c18a85dec800225d39dd13fd2f86d206cb30977967867dabae86af79f60bfe1') {
    pending.push(import('./chunks/chunk-7f897c824043508865aaeefd951f442272e167c57ebfc0e5e651eb6a2f0c749f.js'));
  }
  if (key === '6fbb31d0d149d82dfd5b264d248e7b22cf30b4180f6c33ba2377c44b918e7c68') {
    pending.push(import('./chunks/chunk-d51db4f721cd90a37ecf7b742fdc7ed2edadce5d224a3eb8dabb8ef7c33bfb5a.js'));
  }
  if (key === '53d441e185798070783f0abfc12c62ab5aba3dba05a83bce1c2812ffa2122a8e') {
    pending.push(import('./chunks/chunk-2b916e01a2644eea96c720354cc50c72ec659aed26f4199d9a5e4e73586f2833.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}