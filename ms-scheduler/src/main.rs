use std::env;

use dotenv::dotenv;

use crate::infrastructure::config_client_service;
use crate::infrastructure::config_client_service::ConfigClientService;

mod infrastructure;
mod domain;
mod application;

#[tokio::main]
async fn main(){
    println!("Hello, world!");

    setup_app_config().await;
    setup_eureka_client().await;
}

async fn setup_app_config() {
    if let None = dotenv().ok() {
        panic!("No .env file found. Cannot start the app")
    }

    let mut config_client = ConfigClientService::default();
    let config = config_client.obtain_config().await.unwrap_or_else(|error| {
        panic!("Error obtaining config from config server. Error: {:?}", error)
    });

    println!("Config: {:?}", &config);

    config_client_service::setup_environment_variables(config);
}

async fn setup_eureka_client() {

}

// By default return dev
fn set_build_name_env() {
    let args: Vec<String> = env::args().collect();

    let mut build_name = "dev".to_string();

    if let Some(build_arg_index) = args.iter().position(|arg| arg == "--build") {
        if let Some(build_value) = args.get(build_arg_index + 1) {
            build_name = build_value.to_owned()
        }
    }

    env::set_var("build.name", build_name)
}