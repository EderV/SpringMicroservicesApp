use reqwest::Client;
use log::debug;
use crate::domain::entities::eureka::EurekaInstance;

#[derive(Debug)]
pub struct EurekaRestClient {
    client: Client,
    base_url: String,
}

impl EurekaRestClient {
    pub fn new(base_url: String) -> Self {
        EurekaRestClient {
            client: Client::new(),
            base_url,
        }
    }

    /// Register new application instance
    pub fn register(&self, app_id: &str, data: &EurekaInstance) -> Result<(), EurekaError> {
        let url = format!("{}/apps/{}", self.base_url, path_segment_encode(app_id));
        debug!("Sending register request to {}", url);
        let resp = self
            .client
            .post(&url)
            .header("Accept", "application/json")
            .json(&Register { instance: data })
            .send();
        match resp {
            Err(e) => Err(EurekaError::Network(e)),
            Ok(resp) => match resp.status() {
                StatusCode::NO_CONTENT => Ok(()),
                _ => Err(EurekaError::Request(resp.status())),
            },
        }
    }

    /// De-register application instance
    pub fn deregister(&self, app_id: &str, instance_id: &str) -> Result<(), EurekaError> {
        let url = format!(
            "{}/apps/{}/{}",
            self.base_url,
            path_segment_encode(app_id),
            path_segment_encode(instance_id)
        );
        debug!("Sending deregister request to {}", url);
        let resp = self.client.delete(&url).send();
        match resp {
            Err(e) => Err(EurekaError::Network(e)),
            Ok(resp) => match resp.status() {
                StatusCode::OK => Ok(()),
                _ => Err(EurekaError::Request(resp.status())),
            },
        }
    }

    /// Send application instance heartbeat
    pub fn send_heartbeat(&self, app_id: &str, instance_id: &str) -> Result<(), EurekaError> {
        let url = format!(
            "{}/apps/{}/{}",
            self.base_url,
            path_segment_encode(app_id),
            path_segment_encode(instance_id)
        );
        debug!("Sending heartbeat request to {}", url);
        let resp = self
            .client
            .put(&url)
            .header("Accept", "application/json")
            .send();
        match resp {
            Err(e) => Err(EurekaError::Network(e)),
            Ok(resp) => match resp.status() {
                StatusCode::OK => Ok(()),
                StatusCode::NOT_FOUND => Err(EurekaError::UnexpectedState(
                    "Instance does not exist".into(),
                )),
                _ => Err(EurekaError::Request(resp.status())),
            },
        }
    }
}