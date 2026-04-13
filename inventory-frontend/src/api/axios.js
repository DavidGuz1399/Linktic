import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080/api",
});

const inventoryApi = axios.create({
    baseURL: "http://localhost:8081/api",
});

const setupInterceptors = (instance) => {
    instance.interceptors.request.use((config) => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        config.headers["X-Correlation-Id"] = `web-${crypto.randomUUID()}`
        return config;
    });
}

setupInterceptors(api);
setupInterceptors(inventoryApi);

export { api, inventoryApi };