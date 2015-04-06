Intrade::Application.routes.draw do
  resources :users do
    member do
      get :following, :followers
    end
  end
  resources :sessions,      only: [:new, :create, :destroy]
  resources :microposts,    only: [:create, :destroy]
  resources :products
  resources :product_categories
  resources :relationships, only: [:create, :destroy]
  root to: 'products#search'

  match 'addToCart',to: 'products#addToCart',   via: 'get'
  match 'editor',   to: 'editor#index',         via: 'get'
  match '/users/:id/upupdate(.:format)', to: 'users#upupdate', via: 'post'
  match '/products/destroy_product_attribute/:id(.:format)', to: 'products#destroy_product_attribute', via: 'get'
  match '/desktop', to: 'static_pages#desktop', via: 'get'
  match '/search',  to: 'products#search',      via: 'get'
  match '/catalog', to: 'products#catalog',     via: 'get'
  match '/product/:id', to: 'products#view',     via: 'get'
  match '/signup',  to: 'users#new',            via: 'get'
  match '/signin',  to: 'sessions#new',         via: 'get'
  match '/signout', to: 'sessions#destroy',     via: 'delete'
  match '/help',    to: 'static_pages#help',    via: 'get'
  match '/about',   to: 'static_pages#about',   via: 'get'
  match '/contact', to: 'static_pages#contact', via: 'get'
  match '/lamoda', to: 'static_pages#lamoda', via: 'get'
end