import "./App.css"
import { RouterProvider } from "react-router-dom"
import root from "./router/root"
import { Provider } from "react-redux"
import { PersistGate } from "redux-persist/integration/react"
import store, { persistor } from "./store/store"
import ScrollTop from "./util/ScrollTop"

function App() {
  return (
    <>
      <Provider store={store}>
        <PersistGate loading={null} persistor={persistor}>
          <RouterProvider router={root} />
        </PersistGate>
      </Provider>
    </>
  )
}

export default App
