use std::collections::HashMap;
use std::env;
use std::string::ToString;

use reqwest;

use crate::domain::entities::config_server::Config;
use crate::domain::entities::config_client_config::ConfigClientConfig;
use crate::domain::errors::config_client_errors::GetConfigError;

const DEFAULT_CONFIG_SERVER_URL: &str = "http://localhost:8888";

pub struct ConfigClientService {
    config_client_config: ConfigClientConfig
}

impl ConfigClientService {

    pub fn new(config_client_config: ConfigClientConfig) -> Self {
        return ConfigClientService { config_client_config };
    }

    pub async fn obtain_config(&self) -> Result<Config, GetConfigError> {
        let url = &self.config_client_config.url.clone();
        let app_name = &self.config_client_config.app_name.clone();
        let build = &self.config_client_config.build.clone();
        let config_endpoint = format!("{}/{}/{}", url, app_name, build);

        let response = reqwest::get(&config_endpoint).await?;

        if response.status().is_success() {
            let config_text = response.text().await?;

            let config_response: Result<Config, _> = serde_json::from_str(&*config_text);
            return if let Ok(res) = config_response {
                Ok(res)
            } else {
                Err(GetConfigError::DeserializationFailed(config_response.err().unwrap().to_string()))
            }
        } else {
            let error_msg = format!(r#"Error obtaining the configuration. Error code: {}"#, response.status());
            Err(GetConfigError::ConfigServerError(error_msg))
        }
    }

}

impl Default for ConfigClientService {
    fn default() -> Self {
        let config_client_config = ConfigClientConfig {
            app_name: env::var("rust.application.name").unwrap_or_else(|_| { "rust-app".to_string() }),
            url: env::var("spring.config.server.url").unwrap_or_else(|_| { DEFAULT_CONFIG_SERVER_URL.parse().unwrap() }),
            build: env::var("build.name").unwrap_or_else(|_| { "dev".to_string() })
        };
        return ConfigClientService { config_client_config };
    }
}

pub fn setup_environment_variables(config: Config) {
    let property_source = config.property_sources;

    // First add application properties and then specific build properties. Build properties takes preference
    let mut application_properties = HashMap::new();
    let mut build_properties = HashMap::new();

    property_source.iter().for_each(|prop| {
        if prop.name.ends_with("application.yml") {
            application_properties = prop.source.clone();
        }
        else {
            build_properties = prop.source.clone();
        }
    });

    for (key, value) in application_properties.into_iter() {
        env::set_var(key, value)
    }

    for (key, value) in build_properties.into_iter() {
        env::set_var(key, value)
    }
}

// pub async fn download_env_file(
//     url: String,
//     app_name: String,
//     build: String
// ) -> Result<Config, Box<dyn std::error::Error>> {
//     let config_endpoint = format!("{}/{}/{}", url, app_name, build);
//
//     let response = reqwest::get(&config_endpoint).await?;
//
//     if response.status().is_success() {
//         let config_text = response.text().await?;
//
//         println!("Received config: {}", config_text);
//
//         let config_response:Result<Config, _> = serde_json::from_str(&*config_text);
//
//         return if let Ok(res) = config_response {
//             Ok(res)
//         } else {
//             Err(config_response.err().unwrap().into())
//         }
//     } else {
//         println!("Error obtaining the configuration: {}", response.status());
//         Err("Failed to obtain configuration".into())
//     }
// }