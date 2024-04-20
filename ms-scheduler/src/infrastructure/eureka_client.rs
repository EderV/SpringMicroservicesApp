use std::env;

pub struct EurekaClient {

}

impl EurekaClient {

    pub fn new() -> Self {
        EurekaClient {}
    }

    pub async fn start(&self) {
        let mut eureka_server_url= "".to_string();
        if let Some(url) = env::var("EUREKA_SERVER_URL").ok() {
            eureka_server_url = url
        }

        let body = self.create_body();

        self.register_service(&eureka_server_url, &body).await;
    }

    fn create_body(&self) -> String {
        let mut service_name = "".to_string();
        if let Some(name) = env::var("RUST_APPLICATION_NAME").ok() {
            service_name = name;
        }

        let mut server_port = 8080;
        if let Some(port) = env::var("SERVER_PORT").ok() {
            server_port = port.parse::<i32>().unwrap_or(0);
        }

        let ip_address = local_ip_address::local_ip().unwrap().to_string();
        let instance_id = "ms-scheduler-rust";

        return format!(
            r#"
            <instance>
                <instanceId>{}</instanceId>
                <hostName>{}</hostName>
                <app>{}</app>
                <ipAddr>{}</ipAddr>
                <port>{}</port>
                <status>UP</status>
                <dataCenterInfo class="com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo" />
            </instance>
            "#,
            instance_id, ip_address, service_name, ip_address, server_port
        );
    }

    async fn register_service(&self, eureka_server_url: &str, body: &str) {
        let client = reqwest::Client::new();
        let response = client
            .post(eureka_server_url)
            .header("Content-Type", "application/xml")
            .body(body.to_owned())
            .send()
            .await;

        if let Err(err) = response {
            println!("Error al registrar el microservicio en Eureka: {}", err);
        } else {
            println!("Microservicio registrado en Eureka exitosamente. {:?}", response.unwrap());
        }
    }


}