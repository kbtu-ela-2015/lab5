Intrade::Application.routes.draw do
  match '/about', to: 'home#about', via: 'get'
  match '/lab5', to: 'home#lab5',   via: 'post'
end
